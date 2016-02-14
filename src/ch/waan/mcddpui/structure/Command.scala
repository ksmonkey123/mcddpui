package ch.waan.mcddpui.structure

import scala.util.Try

trait CommandDispatcher[T] {
  def dispatch(c: MutationCommand[_ >: T, _ <: T])
  def dispatch(c: ReadCommand[_ >: T])
  def dispatch(c: ManagerCommand)
}

sealed trait Command[-A, +B] {
  def dispatch[T >: B <: A](implicit d: CommandDispatcher[T])
}

trait ReadCommand[-A] extends Command[A, Nothing] {
  final override def dispatch[T >: Nothing <: A](implicit d: CommandDispatcher[T]) =
    d.dispatch(this)

  def apply(a: A): Option[Throwable]
}

object ReadCommand {
  def apply[A](f: A => Option[Throwable])(implicit ev: A <:< Any): ReadCommand[A] = {
    if (f == null) throw new NullPointerException("cannot create [null] command")
    new ReadCommand[A] {
      def apply(a: A): Option[Throwable] = f(a)
    }
  }

  def apply[A](f: A => Unit): ReadCommand[A] = {
    if (f == null) throw new NullPointerException("cannot create [null] command")
    new ReadCommand[A] {
      def apply(a: A): Option[Throwable] = Try(f(a)).failed.toOption
    }
  }

}

trait MutationCommand[-A, +B] extends Command[A, B] {
  final override def dispatch[T >: B <: A](implicit d: CommandDispatcher[T]) =
    d.dispatch(this)

  def apply(a: A): Try[B]

  def andThen[C](c: MutationCommand[_ >: B, C]) = {
    if (c == null)
      throw new NullPointerException("cannot concatenate with [null] command")
    MutationCommand(apply(_: A) flatMap c.apply)
  }
}

object MutationCommand {
  def apply[A, B](f: A => Try[B])(implicit ev: A <:< Any): MutationCommand[A, B] = {
    if (f == null) throw new NullPointerException("cannot create [null] command")
    new MutationCommand[A, B] {
      def apply(a: A) = f(a)
    }
  }
  def apply[A, B](f: A => B): MutationCommand[A, B] = {
    if (f == null) throw new NullPointerException("cannot create [null] command")
    new MutationCommand[A, B] {
      def apply(a: A) = Try(f(a))
    }
  }
}

sealed trait ManagerCommand extends Command[Any, Nothing] {
  final override def dispatch[T >: Nothing <: Any](implicit d: CommandDispatcher[T]) =
    d.dispatch(this)
}

case class ErrorCommand(error: Throwable) extends ManagerCommand
case object UndoCommand extends ManagerCommand
case object RedoCommand extends ManagerCommand