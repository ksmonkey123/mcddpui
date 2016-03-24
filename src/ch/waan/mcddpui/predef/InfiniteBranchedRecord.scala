package ch.waan.mcddpui.predef

import ch.waan.mcddpui.api.Record
import ch.waan.mcddpui.api.MutationCommand
import ch.waan.mcddpui.exceptions.RecordHistoryManipulationException

class InfiniteBranchedRecord[T](initial: T) extends Record[T] {

    private case class Cell(name: String, state: T, branches: List[List[Cell]])

    private[this] val LOCKER = new Object
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