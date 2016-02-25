package ch.waan.mcddpui.predef

import ch.waan.mcddpui.api.Record
import ch.waan.mcddpui.api.CommandExecutor
import ch.waan.mcddpui.api.ReadCommand
import ch.waan.mcddpui.api.MutationCommand
import ch.waan.mcddpui.api.ManagerCommand
import ch.waan.mcddpui.api.UndoCommand
import ch.waan.mcddpui.api.RedoCommand

class RecordCommandExecutor[T](record: Record[T]) extends CommandExecutor[T] {

    @throws(classOf[Throwable])
    override def apply(c: ReadCommand[_ >: T]): Unit = record.view(c)

    @throws(classOf[Throwable])
    override def apply(c: MutationCommand[_ >: T, _ <: T]): Unit = record.update(c)

    @throws(classOf[Throwable])
    override def apply(c: ManagerCommand): Unit = c match {
        case UndoCommand => record.undo()
        case RedoCommand => record.redo()
    }

}