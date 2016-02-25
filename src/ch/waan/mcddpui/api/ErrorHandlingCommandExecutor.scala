package ch.waan.mcddpui.api;

import scala.util.control.NonFatal

abstract class ErrorHandlingCommandExecutor[T](executor: CommandExecutor[T]) extends CommandExecutor[T] {

    override def apply(c: ReadCommand[_ >: T]): Unit =
        try {
            executor(c)
        } catch {
            case NonFatal(e) => handleException(ReadCommandExecutionException(e))
        }

    override def apply(c: MutationCommand[_ >: T, _ <: T]): Unit =
        try {
            executor(c)
        } catch {
            case NonFatal(e) => handleException(MutationCommandExecutionException(e))
        }

    override def apply(c: ManagerCommand): Unit =
        try {
            executor(c)
        } catch {
            case NonFatal(e) => handleException(ManagerCommandExecutionException(e))
        }

    def handleException(ex: CommandExecutionException): Unit;

}