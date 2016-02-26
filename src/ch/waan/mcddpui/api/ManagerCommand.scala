package ch.waan.mcddpui.api

trait ManagerCommand

object ManagerCommand {
    case object UndoCommand extends ManagerCommand
    case object RedoCommand extends ManagerCommand
}
