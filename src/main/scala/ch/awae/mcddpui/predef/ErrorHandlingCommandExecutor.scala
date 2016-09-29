package ch.awae.mcddpui.predef

import ch.awae.mcddpui.api.CommandExecutor
import ch.awae.mcddpui.api.ManagerCommand
import ch.awae.mcddpui.api.MutationCommand
import ch.awae.mcddpui.api.ReadCommand
import ch.awae.mcddpui.exceptions.CommandExecutionException
import ch.awae.mcddpui.exceptions.ManagerCommandExecutionException
import ch.awae.mcddpui.exceptions.MutationCommandExecutionException
import ch.awae.mcddpui.exceptions.ReadCommandExecutionException

/**
 * A [[ch.waan.mcddpui.api.CommandExecutor CommandExecutor]] with integrated
 * exception handling.
 *
 * This executor is backed by any arbitrary executor that does the actual
 * operation. This executor serves as a wrapping layer that catches all
 * exceptions thrown by the backing executor and passes them into a handling
 * method.
 *
 * @tparam T the data structure the executor operates on
 * @param executor the backing executor
 * @constructor creates a new executor instance
 *
 * @author Andreas Waelchli <andreas.waelchli@me.com>
 * @version 1.1 (0.1.0), 2016-02-29
 * @since MCDDPUI 0.1.0
 */
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

    /**
     * handles any [[ch.waan.mcddpui.exceptions.CommandExecutionException CommandExecutionException]]
     * thrown while executing any command.
     *
     * @param ex CommandExecutionException the exception to handle
     */
    def handleException(ex: CommandExecutionException): Unit;

}