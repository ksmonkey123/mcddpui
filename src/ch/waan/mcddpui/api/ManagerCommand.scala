package ch.waan.mcddpui.api

trait ManagerCommand extends Command

object ManagerCommand {
    case object UndoCommand extends ManagerCommand
    case object RedoCommand extends ManagerCommand
}
