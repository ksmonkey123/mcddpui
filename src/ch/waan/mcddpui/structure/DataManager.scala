package ch.waan.mcddpui.structure

import java.util.Objects

trait DataManager[T] extends CommandDispatcher[T] {

  protected def record: Record[T]
  protected def refresh(t: T): Option[Throwable]
  protected def handleError(c: Command[_, _], t: Throwable): Unit

  def dispatch(c: MutationCommand[_ >: T, _ <: T]): Unit = {
    if (c == null)
      dispatch(ErrorCommand(new NullPointerException("[null] command not allowed")))
    record.update(x => c.apply(x)) match {
      case Some(t) => handleError(c, t)
      case None => dispatch(ReadCommand(refresh(_)))
    }
  }

  def dispatch(c: ReadCommand[_ >: T]): Unit = {
    if (c == null)
      dispatch(ErrorCommand(new NullPointerException("[null] command not allowed")))
    record.passTo(c.apply(_)).foreach(x => handleError(c, x))
  }

  def dispatch(c: ManagerCommand): Unit = c match {
    case null => dispatch(ErrorCommand(new NullPointerException("[null] command not allowed")))
    case ErrorCommand(t) => handleError(c, t)
    case UndoCommand => record.undo.foreach(handleError(c, _))
    case RedoCommand => record.redo.foreach(handleError(c, _))
  }

}

object DataManager {

  def apply[T](rec: Record[T], errorHandler: ((Command[_, _], Throwable, CommandDispatcher[T]) => Unit), refreshCommand: ReadCommand[T]) = {
    Objects.requireNonNull(rec, "[null] record not allowed")
    Objects.requireNonNull(errorHandler, "[null] error handler not allowed")

    new DataManager[T] {
      val record = rec
      def handleError(c: Command[_, _], t: Throwable) = errorHandler(c, t, this)
      def refresh(u: T) = if (refreshCommand == null) None else refreshCommand(u)
    }
  }

}