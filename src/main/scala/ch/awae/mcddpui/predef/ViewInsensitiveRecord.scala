package ch.awae.mcddpui.predef

import ch.awae.mcddpui.api.MutationCommand
import ch.awae.mcddpui.api.ReadCommand
import ch.awae.mcddpui.api.Record
import ch.awae.mcddpui.api.UIUniverse
import ch.awae.mcddpui.api.function2readCommand
import ch.awae.mcddpui.api.functionNameTuple2mutationCommand
import ch.awae.util.function.Id
import ch.awae.util.function.{ ~> => ~> }

case class ViewInsensitiveRecord[Model] private (private val backer: Record[ViewInsensitiveRecord.Box[UIUniverse[Model]]]) extends Record[UIUniverse[Model]] {

    def listRedoPaths: List[String] = backer.listRedoPaths
    def redo(index: Int): Unit = backer.redo(index)
    def undo: Unit = backer.undo
    def view(c: ReadCommand[_ >: UIUniverse[Model]]): Unit = backer.view((box: ViewInsensitiveRecord.Box[UIUniverse[Model]]) => c(box.item))

    def update(c: MutationCommand[_ >: UIUniverse[Model], _ <: UIUniverse[Model]]): Unit = {
        // run mutation & then check if it has to be historized
        backer.view((box: ViewInsensitiveRecord.Box[UIUniverse[Model]]) => {
            val next = c(box.item)
            if (next.data == box.item.data
                && next.ui.props == box.item.ui.props) {
                box.item = next
            } else {
                backer.update(((box: ViewInsensitiveRecord.Box[UIUniverse[Model]]) => new ViewInsensitiveRecord.Box(next), c.name))
            }
        })
    }

}

object ViewInsensitiveRecord {

    def apply[Model](wrapper: Id ~> Record, initial: UIUniverse[Model]): ViewInsensitiveRecord[Model] = ViewInsensitiveRecord(wrapper(new Box(initial)))

    @SerialVersionUID(0)
    protected class Box[T](var item: T) extends Serializable

}