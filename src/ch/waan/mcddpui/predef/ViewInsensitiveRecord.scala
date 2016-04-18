package ch.waan.mcddpui.predef

import ch.waan.mcddpui.api.MutationCommand
import ch.waan.mcddpui.api.ReadCommand
import ch.waan.mcddpui.api.Record
import ch.waan.mcddpui.api.UIUniverse
import ch.waan.mcddpui.api.function2readCommand
import ch.waan.mcddpui.api.functionNameTuple2mutationCommand

@SerialVersionUID(0)
class Box[T](var item: T) extends Serializable

case class ViewInsensitiveRecord[Model](private val backer: Record[Box[UIUniverse[Model]]]) extends Record[UIUniverse[Model]] {

    def listRedoPaths: List[String] = backer.listRedoPaths
    def redo(index: Int): Unit = backer.redo(index)
    def undo: Unit = backer.undo
    def view(c: ReadCommand[_ >: UIUniverse[Model]]): Unit = backer.view((box: Box[UIUniverse[Model]]) => c(box.item))

    def update(c: MutationCommand[_ >: UIUniverse[Model], _ <: UIUniverse[Model]]): Unit = {
        // run mutation & then check if it has to be historized
        backer.view((box: Box[UIUniverse[Model]]) => {
            val next = c(box.item)

            if (next.data == box.item.data
                && next.ui.props == box.item.ui.props) {
                println("viewstack update")
                box.item = next
            } else {
                println("tracked update")
                backer.update(((box: Box[UIUniverse[Model]]) => new Box(next), c.name))
            }
        })
    }

}