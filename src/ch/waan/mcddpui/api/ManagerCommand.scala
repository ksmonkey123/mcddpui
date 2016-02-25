package ch.waan.mcddpui.api

trait ManagerCommand

case object UndoCommand extends ManagerCommand
case object RedoCommand extends ManagerCommand

object ManagerCommands {

    val UndoCommand = ch.waan.mcddpui.api.UndoCommand
    val RedoCommand = ch.waan.mcddpui.api.RedoCommand

}