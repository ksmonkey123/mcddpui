package ch.awae.util

import scala.concurrent.Future
import scala.concurrent.Promise
import scala.concurrent.ExecutionContext.Implicits.global

sealed trait AutoInit[-Input, +Type] {
  // getters
  def get: Type
  def option: Option[Type]
  def future: Future[Type]
  // initializers
  def apply(input: Input): Type
  def apply(input: Future[Input]): Future[Type]
}

object AutoInit {

  def apply[Input, Type](code: Input => Type): AutoInit[Input, Type] = new AutoInitImpl(code)

  private sealed trait AutoInitState
  private case object NotInitialized extends AutoInitState
  private case object InitializedOverFuture extends AutoInitState
  private case object Initialized extends AutoInitState
  private case class InitialisationFailed(ex: Throwable) extends AutoInitState

  private class AutoInitImpl[-Input, +Type] private[this] (promise: Promise[Type], code: Input => Type) extends AutoInit[Input, Type] {
    def this(code: Input => Type) = this(Promise(), code)
    private[this] var state: AutoInitState = NotInitialized
    private[this] var value: Type = _
    val future = promise.future
    assert(code != null, "initialisation code is 'null'. This is not allowed")
    // getters
    def get = state match {
      case Initialized => value
      case InitialisationFailed(ex) => throw new IllegalStateException("initialisation failed", ex)
      case _ => throw new IllegalStateException("not yet initialized")
    }
    def option =
      if (state equals Initialized) Option(value)
      else Option.empty
    // initializers
    def apply(input: Input): Type = this synchronized {
      state match {
        case NotInitialized =>
          value = code(input)
          state = Initialized
          promise.success(value)
          value
        case InitializedOverFuture => throw new IllegalStateException("already bound to be completed on a future")
        case _ => throw new IllegalStateException("already initialized")
      }
    }
    def apply(input: Future[Input]): Future[Type] = this synchronized {
      assert(input != null, "'null' input future not allowed")
      state match {
        case NotInitialized =>
          state = InitializedOverFuture
          val fut = input.map(code(_))
          promise.completeWith(fut)
          fut onSuccess {
            case t =>
              value = t
              state = Initialized
          }
          fut onFailure {
            case ex =>
              state = InitialisationFailed(ex)
          }
          future
        case InitializedOverFuture => throw new IllegalStateException("already bound to a future for completion")
        case _ => throw new IllegalStateException("already initialized")
      }
    }
  }

}