package ch.waan.mcddpui.api

trait Record[T] {

    @throws(classOf[Throwable])
    def view(c: ReadCommand[_ >: T]): Unit

    @throws(classOf[Throwable])
    def update(c: MutationCommand[_ >: T, _ <: T]): Unit

    @throws(classOf[RecordHistoryManipulationException])
    def undo: Unit

    @throws(classOf[RecordHistoryManipulationException])
    def redo: Unit

}