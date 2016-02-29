package ch.waan.mcddpui.predef

import ch.waan.mcddpui.api.Record
import ch.waan.mcddpui.exceptions.RecordHistoryManipulationException

/**
 * a record with an infinitely long history.
 *
 * All operations are synchronized and therefore thread-safe
 *
 * @tparam T the data type of the internal data structure
 *
 * @constructor creates a new instance
 * @param initial the initial value for the internal data struture
 * 
 * @author Andreas Waelchli <andreas.waelchli@me.com>
 * @version 1.1 (0.1.0), 2016-02-29
 * @since MCDDPUI 0.1.0
 */
class InfiniteRecord[T](initial: T) extends Record[T] {

    private[this] val LOCKER = new Object
    private[this] var history = List(initial)
    private[this] var redoStack: List[T] = Nil

    override def view(f: ch.waan.mcddpui.api.ReadCommand[_ >: T]): Unit =
        f(history.head)

    override def update(f: ch.waan.mcddpui.api.MutationCommand[_ >: T, _ <: T]): Unit = LOCKER synchronized {
        val next = f(history.head)
        history ::= next
        redoStack = Nil
    }

    override def undo(): Unit = LOCKER synchronized {
        if (history.tail.isEmpty)
            throw new RecordHistoryManipulationException("cannot undo empty history")
        redoStack ::= history.head
        history = history.tail
    }

    override def redo(): Unit = LOCKER synchronized {
        if (redoStack.isEmpty)
            throw new RecordHistoryManipulationException("cannot redo")
        history ::= redoStack.head
        redoStack = redoStack.tail
    }

}