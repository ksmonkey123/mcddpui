package ch.waan.mcddpui.api

import scala.util.control.NonFatal

object ThrowFatal {

    def apply(th: Throwable) = th match {
        case NonFatal(t) => t
        case t           => throw t
    }

}

class CommandExecutionException(cause: Throwable) extends Exception(cause)

case class RecordHistoryManipulationException(msg: String) extends Exception(msg)

case class ReadCommandExecutionException(cause: Throwable) extends CommandExecutionException(cause)
case class MutationCommandExecutionException(cause: Throwable) extends CommandExecutionException(cause)
case class ManagerCommandExecutionException(cause: Throwable) extends CommandExecutionException(cause)