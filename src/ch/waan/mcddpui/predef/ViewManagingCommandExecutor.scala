package ch.waan.mcddpui.predef

import java.util.Objects

import scala.util.control.NonFatal

import ch.waan.mcddpui.api.ManagerCommand
import ch.waan.mcddpui.api.MutationCommand
import ch.waan.mcddpui.api.Record
import ch.waan.mcddpui.api.UIUniverse
import ch.waan.mcddpui.api.View
import ch.waan.mcddpui.api.ViewManager
import ch.waan.mcddpui.api.ViewManagerLike
import ch.waan.mcddpui.api.function2readCommand
import ch.waan.mcddpui.exceptions.CommandExecutionException
import ch.waan.mcddpui.exceptions.RecordHistoryManipulationException

/**
 * A command executor internally holding and maintaining a ViewManager.
 *
 * This class serves as a combination of both a commandExecutor and a ViewManager.
 * Error Handling and an abstract method for custom update code (that executes along side
 * the internal view updates) is provided.
 *
 * @author Andreas Waelchli <andreas.waelchli@me.com>
 * @version 1.1 (0.3.0), 2016-03-24
 * @since MCDDPUI 0.3.0
 *
 * @tparam T the type of the data structure
 *
 * @param record the [[Record]] the instance should operate on
 * @throws NullPointerException if `record` is `null`
 */
@throws[NullPointerException]
abstract class ViewManagingCommandExecutor[T](val record: Record[UIUniverse[T]])
        extends ErrorHandlingCommandExecutor[UIUniverse[T]](new RecordCommandExecutor(record)) with ViewManagerLike[T] {
    Objects.requireNonNull(record)

    private[this] val viewManager = new ViewManager(this)

    override def apply(c: ManagerCommand): Unit = {
        super.apply(c)
        record view { (u: UIUniverse[T]) =>
            update(u)
            return
        }
    }

    override def apply(c: MutationCommand[_ >: UIUniverse[T], _ <: UIUniverse[T]]): Unit = {
        super.apply(c)
        try {
            record view { (u: UIUniverse[T]) =>
                update(u)
                return
            }
        } catch {
            case NonFatal(t) =>
                record.undo // universe update is done. Undo changes
                handleException(new CommandExecutionException(t))
        }
    }

    @throws[NullPointerException]
    @throws[IllegalArgumentException]
    override def register(view: View[T]): Unit = viewManager.register(view)

    @throws[Throwable]
    override def update(universe: UIUniverse[T]) = {
        val res = viewManager.update(universe)
        customUpdate(universe)
        res
    }

    /**
     * Custom Update method
     *
     * This method is invoked alongside the internal updates of the views.
     * This allows for updates of UI elements that are not directly associated with
     * a view (e.g. a tool-bar in a window shared between views)
     *
     * @throws Throwable if any exceptional condition occurs
     */
    @throws[Throwable]
    def customUpdate(universe: UIUniverse[T]): Unit

}