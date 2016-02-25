package ch.waan.mcddpui.api

trait CommandExecutor[T] {

    @throws(classOf[Throwable])
    def apply(c: ReadCommand[_ >: T]): Unit

    @throws(classOf[Throwable])
    def apply(c: MutationCommand[_ >: T, _ <: T]): Unit

    @throws(classOf[Throwable])
    def apply(c: ManagerCommand): Unit

}