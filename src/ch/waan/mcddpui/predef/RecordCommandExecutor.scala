package ch.waan.mcddpui.predef

import ch.waan.mcddpui.api.CommandExecutor
import ch.waan.mcddpui.api.ManagerCommand
import ch.waan.mcddpui.api.RedoCommand
import ch.waan.mcddpui.api.UndoCommand
import ch.waan.mcddpui.api.MutationCommand
import ch.waan.mcddpui.api.ReadCommand
import ch.waan.mcddpui.api.Record
import ch.waan.mcddpui.exceptions.ManagerCommandExecutionException
import ch.waan.mcddpui.exceptions.MutationCommandExecutionException
import ch.waan.mcddpui.exceptions.ReadCommandExecutionException
import ch.waan.mcddpui.exceptions.RecordHistoryManipulationException

/**
 * a [[ch.waan.mcddpui.api.CommandExecutor CommandExecutor]] operating on
 * a [[ch.waan.mcddpui.api.Record Record]]
 *
 * @tparam T the data type of the internal data structure of the backing record
 * @param record the [[ch.waan.mcddpui.api.Record Record]] this executor should operate on
 * @constructor creates a new executor instance
 *
 * @author Andreas Waelchli <andreas.waelchli@me.com>
 * @version 1.2 (0.2.0), 2016-03-01
 * @since MCDDPUI 0.1.0
 */
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