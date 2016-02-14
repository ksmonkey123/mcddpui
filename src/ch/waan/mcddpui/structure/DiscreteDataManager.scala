package ch.waan.mcddpui.structure

class DiscreteDataManager[T](
  protected val record: Record[T],
  errorHandler: ((Command[_, _], Throwable, CommandDispatcher[T]) => Unit),
  refreshCommand: ReadCommand[T])
    extends DataManager[T] {

  override protected def handleError(c: Command[_, _], t: Throwable) =
    errorHandler(c, t, this)

  override protected def refresh(t: T) =
    refreshCommand(t)

}