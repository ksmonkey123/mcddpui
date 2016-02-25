package ch.waan.mcddpui.api

@FunctionalInterface
trait ReadCommand[T] {

    @throws(classOf[Throwable])
    def apply(t: T): Unit

}