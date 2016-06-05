package ch.waan.mcddpui.predef

import ch.waan.mcddpui.api.MutationCommand
import ch.waan.mcddpui.api.Record
import ch.waan.mcddpui.exceptions.RecordHistoryManipulationException
import ch.waan.util.function.Id
import ch.waan.util.function.{ ~> => ~> }
import ch.waan.util.function.FunctorProvider

/**
 * a record with an infinitely long history that supports an arbitrary number of branching redo paths.
 *
 * All operations are synchronised and therefore thread-safe
 *
 * @tparam T the data type of the internal data structure
 *
 * @constructor creates a new instance
 * @param initial the initial value for the internal data structure
 *
 * @author Andreas Waelchli <andreas.waelchli@me.com>
 * @version 1.1 (0.3.0), 2016-03-24
 * @since MCDDPUI 0.3.0
 */
@SerialVersionUID(0L)
class InfiniteBranchedRecord[T](initial: T) extends Record[T] {

    private case class Cell(name: String, state: T, branches: List[List[Cell]])

    @transient private[this] val LOCKER = new Object
    private[this] var history = List(Cell(null, initial, Nil))
    private[this] var redo = Nil: List[Cell]

    override def view(f: ch.waan.mcddpui.api.ReadCommand[_ >: T]): Unit =
        f(history.head.state)

    override def update(c: MutationCommand[_ >: T, _ <: T]): Unit = LOCKER synchronized {
        val next = Cell(c.name, c(history.head.state), Nil)
        if (redo.isEmpty)
            history ::= next
        else {
            history = next :: history.head.copy(branches = redo :: history.head.branches) :: history.tail
            redo = Nil
        }
    }

    override def undo(): Unit = LOCKER synchronized {
        if (history.tail.isEmpty)
            throw new RecordHistoryManipulationException("cannot undo. History is empty")
        redo ::= history.head
        history = history.tail
    }

    override def listRedoPaths: List[String] =
        if (redo.isEmpty)
            List.empty
        else
            redo.head.name :: history.head.branches.map(_.head.name)

    override def redo(index: Int) = LOCKER synchronized {
        if (redo.isEmpty)
            throw new RecordHistoryManipulationException("cannot redo")
        if ((index < 0) || (index > history.head.branches.size))
            throw new IndexOutOfBoundsException(s"index out of bounds: $index range: [0, ${history.head.branches.size}]")
        if (index == 0) {
            history ::= redo.head
            redo = redo.tail
        } else {
            val theRedo = history.head.branches(index - 1)
            val redos = redo :: history.head.branches.slice(0, index - 1) ::: history.head.branches.drop(index)
            history = theRedo.head :: history.head.copy(branches = redos) :: history.tail
            redo = theRedo.tail
        }
    }

}

object InfiniteBranchedRecord extends (Id ~> InfiniteBranchedRecord) {

    def apply[T](initial: T) = new InfiniteBranchedRecord(initial)

}