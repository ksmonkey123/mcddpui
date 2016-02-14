package ch.waan.mcddpui.structure

import scala.util.Try
import scala.util.Failure
import scala.util.Success

class InfiniteRecord[T](initial: T) extends Record[T] {

  @volatile private[this] var history = List(initial)
  @volatile private[this] var redoStack = List.empty[T]

  private[this] val LOCK = new Object

  override def update(f: T => Try[T]): Option[Throwable] = LOCK synchronized {
    if (f == null)
      Option(new NullPointerException("update function [null] not allowed!"))
    else
      f(history.head) match {
        case Failure(t) => Some(t)
        case Success(v) =>
          history = v :: history
          redoStack = List.empty
          None
      }
  }

  override def passTo(f: T => Option[Throwable]): Option[Throwable] =
    if (f == null)
      Some(new NullPointerException("consumer function [null] not allowed!"))
    else
      f(LOCK synchronized history.head)

  override def undo = LOCK synchronized {
    if (history.size > 1) {
      redoStack = history.head :: redoStack
      history = history.tail
      None
    } else Some(new RecordHistoryManipulationException("history empty. cannot undo"))
  }

  override def redo = LOCK synchronized {
    if (redoStack.size > 0) {
      history = redoStack.head :: history
      redoStack = redoStack.tail
      None
    } else Some(new RecordHistoryManipulationException("redo stack empty. cannot redo"))
  }

}