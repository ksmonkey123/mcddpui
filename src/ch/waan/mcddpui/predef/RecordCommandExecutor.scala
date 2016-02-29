package ch.waan.mcddpui.predef

import ch.waan.mcddpui.api.CommandExecutor
import ch.waan.mcddpui.api.ManagerCommand
import ch.waan.mcddpui.api.ManagerCommand.RedoCommand
import ch.waan.mcddpui.api.ManagerCommand.UndoCommand
import ch.waan.mcddpui.api.MutationCommand
import ch.waan.mcddpui.api.ReadCommand
import ch.waan.mcddpui.api.Record
import ch.waan.mcddpui.exceptions.RecordHistoryManipulationException
import ch.waan.mcddpui.exceptions.ReadCommandExecutionException
import ch.waan.mcddpui.exceptions.MutationCommandExecutionException
import ch.waan.mcddpui.exceptions.ManagerCommandExecutionException
import ch.waan.mcddpui.exceptions.RecordHistoryManipulationException
import ch.waan.mcddpui.exceptions.ManagerCommandExecutionException

class RecordCommandExecutor[T](record: Record[T]) extends CommandExecutor[T] {

    @throws(classOf[ReadCommandExecutionException])
    override def apply(c: ReadCommand[_ >: T]): Unit = record.view(c)

    @throws(classOf[MutationCommandExecutionException])
    override def apply(c: MutationCommand[_ >: T, _ <: T]): Unit = record.update(c)

    @throws(classOf[ManagerCommandExecutionException])
    override def apply(c: ManagerCommand): Unit =
        try {
            c match {
                case UndoCommand => record.undo
                case RedoCommand => record.redo
            }
        } catch {
            case r: RecordHistoryManipulationException =>
                throw new ManagerCommandExecutionException(r)
        }

}