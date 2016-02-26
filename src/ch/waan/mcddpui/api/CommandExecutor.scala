package ch.waan.mcddpui.api

/**
 * A handler that accepts commands and executes them.
 * 
 * @author Andreas Waelchli <andreas.waelchli@me.com>
 * @version 1.1 (0.1.0), 2016-02-26
 * @since MCDDPUI 0.1.0
 * 
 * @tparam T the type of the data structure the executor
 * 				operates on
 */
trait CommandExecutor[T] {

    @throws(classOf[Throwable])
    def apply(c: ReadCommand[_ >: T]): Unit

    @throws(classOf[Throwable])
    def apply(c: MutationCommand[_ >: T, _ <: T]): Unit

    @throws(classOf[Throwable])
    def apply(c: ManagerCommand): Unit

}