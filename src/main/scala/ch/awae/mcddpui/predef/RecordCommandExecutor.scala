package ch.awae.mcddpui.predef

import ch.awae.mcddpui.api.CommandExecutor
import ch.awae.mcddpui.api.ManagerCommand
import ch.awae.mcddpui.api.RedoCommand
import ch.awae.mcddpui.api.UndoCommand
import ch.awae.mcddpui.api.MutationCommand
import ch.awae.mcddpui.api.ReadCommand
import ch.awae.mcddpui.api.Record
import ch.awae.mcddpui.exceptions.ManagerCommandExecutionException
import ch.awae.mcddpui.exceptions.MutationCommandExecutionException
import ch.awae.mcddpui.exceptions.ReadCommandExecutionException
import ch.awae.mcddpui.exceptions.RecordHistoryManipulationException

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
                case UndoCommand    => record.undo
                case RedoCommand(i) => record.redo(i)
            }
        } catch {
            case i: IndexOutOfBoundsException =>
                throw new ManagerCommandExecutionException(i)
            case r: RecordHistoryManipulationException =>
                throw new ManagerCommandExecutionException(r)
        }

}