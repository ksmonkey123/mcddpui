package ch.waan.mcddpui.predef

import scala.util.control.NonFatal
import ch.waan.mcddpui.exceptions.ReadCommandExecutionException
import ch.waan.mcddpui.exceptions.MutationCommandExecutionException
import ch.waan.mcddpui.exceptions.ManagerCommandExecutionException
import ch.waan.mcddpui.exceptions.CommandExecutionException
import ch.waan.mcddpui.api.CommandExecutor
import ch.waan.mcddpui.api.ManagerCommand
import ch.waan.mcddpui.api.MutationCommand
import ch.waan.mcddpui.api.ReadCommand
import ch.waan.mcddpui.exceptions.MutationCommandExecutionException
import ch.waan.mcddpui.exceptions.ManagerCommandExecutionException

abstract class ErrorHandlingCommandExecutor[T](executor: CommandExecutor[T]) extends CommandExecutor[T] {

    override def apply(c: ReadCommand[_ >: T]): Unit =
        try {
            executor(c)
        } catch {
            case e: CommandExecutionException => handleException(e)
        }

    override def apply(c: MutationCommand[_ >: T, _ <: T]): Unit =
        try {
            executor(c)
        } catch {
            case e: MutationCommandExecutionException => handleException(e)
        }

    override def apply(c: ManagerCommand): Unit =
        try {
            executor(c)
        } catch {
            case e: ManagerCommandExecutionException => handleException(e)
        }

    def handleException(ex: CommandExecutionException): Unit;

}