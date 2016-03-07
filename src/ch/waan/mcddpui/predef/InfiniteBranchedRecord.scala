package ch.waan.mcddpui.predef

import ch.waan.mcddpui.api.Record
import ch.waan.mcddpui.api.MutationCommand
import ch.waan.mcddpui.exceptions.RecordHistoryManipulationException

class InfiniteBranchedRecord[T](initial: T) extends Record[T] {

    private case class CONS(name: String, head: T, tail: CONS, branches: List[CONS]) {
        def ::(t: (String, T)) = CONS(t._1, t._2, this, List.empty)
        def ::(t: (String, T, List[CONS])) = CONS(t._1, t._2, this, t._3)
        def data = (name, head, branches)
    }

    private[this] val LOCKER = new Object
    private[this] var history = CONS(null, initial, null, List.empty)
    private[this] var redo = null: CONS

    override def view(f: ch.waan.mcddpui.api.ReadCommand[_ >: T]): Unit =
        f(history.head)

    override def update(c: MutationCommand[_ >: T, _ <: T]): Unit = LOCKER synchronized {
        val next = c(history.head)
        if (redo == null)
            history ::= (c.name, next)
        else {
            history = (c.name, next) :: (history.name, history.head, redo :: history.branches) :: history.tail
            redo = null
        }
    }

    override def undo(): Unit = LOCKER synchronized {
        if (history.tail == null)
            throw new RecordHistoryManipulationException("cannot undo. History is empty")
        redo ::= history.data
        history = history.tail
    }

    override def listRedoPaths: List[String] =
        if (redo == null)
            List.empty
        else
            redo.name :: history.branches.map(_.name)

    override def redo(index: Int) = LOCKER synchronized {
        if (redo == null)
            throw new RecordHistoryManipulationException("cannot redo")
        if ((index < 0) || (index > history.branches.size))
            throw new IndexOutOfBoundsException(s"index out of bounds: $index range: [0, ${history.branches.size}]")
        if (index == 0) {
            history ::= redo.data
            redo = redo.tail
        } else {
            val theRedo = history.branches(index - 1)
            history ::= (theRedo.name, theRedo.head, redo :: (history.branches.slice(0, index - 1) ::: history.branches.drop(index)))
            redo = theRedo.tail
        }
    }
}