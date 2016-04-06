package ch.waan.mcddpui.predef

import ch.waan.mcddpui.api.Record
import ch.waan.mcddpui.exceptions.RecordHistoryManipulationException

/**
 * a record with an infinitely long history that only supports a single redo path.
 *
 * All operations are synchronised and therefore thread-safe
 *
 * @tparam T the data type of the internal data structure
 *
 * @constructor creates a new instance
 * @param initial the initial value for the internal data structure
 *
 * @author Andreas Waelchli <andreas.waelchli@me.com>
 * @version 1.1 (0.1.0), 2016-02-29
 * @since MCDDPUI 0.1.0
 */
@SerialVersionUID(0L)
class InfiniteLinearRecord[T](initial: T) extends Record[T] {

    @transient private[this] val LOCKER = new Object
    private[this] var history = List[(String, T)]((null, initial))
    private[this] var redoStack: List[(String, T)] = Nil

    override def view(f: ch.waan.mcddpui.api.ReadCommand[_ >: T]): Unit =
        f(history.head._2)

    override def update(f: ch.waan.mcddpui.api.MutationCommand[_ >: T, _ <: T]): Unit = LOCKER synchronized {
        val next = f(history.head._2)
        history ::= f.name -> next
        redoStack = Nil
    }

    override def undo(): Unit = LOCKER synchronized {
        if (history.tail.isEmpty)
            throw new RecordHistoryManipulationException("cannot undo empty history")
        redoStack ::= history.head
        history = history.tail
    }

    override def redo(index: Int): Unit = LOCKER synchronized {
        if (redoStack.isEmpty)
            throw new RecordHistoryManipulationException("cannot redo")
        if ((index < 0) || (index >= redoStack.size))
            throw new IndexOutOfBoundsException(s"index out of bounds: $index != 0]")
        history ::= redoStack.head
        redoStack = redoStack.tail
    }

    override def listRedoPaths = redoStack.map(_._1)

}