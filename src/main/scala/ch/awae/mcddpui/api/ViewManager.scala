package ch.awae.mcddpui.api

import scala.annotation.tailrec
import scala.collection.immutable.Nil
import ch.awae.mcddpui.exceptions.ViewUpdateException
import java.util.Objects

/**
 * core view life-cycle management
 *
 * @author Andreas Waelchli <andreas.waelchli@me.com>
 * @version 1.2 (0.3.0), 2016-03-24
 * @since MCDDPUI 0.2.0
 *
 * @tparam T the type of the data structure
 *
 * @constructor creates a new view manager instance
 * @param executor the executor to bind views to. may not be `null`
 * @throws NullPointerException if the `executor` is `null`
 *
 * @see [[View]] for full life-cycle documentation
 */
@throws[NullPointerException]
final class ViewManager[T](executor: CommandExecutor[UIUniverse[T]]) extends ViewManagerLike[T] {
    Objects.requireNonNull(executor)

    @volatile private var views: List[View[T]] = List.empty
    private val LOCK = new Object

    /**
     * register a view.
     *
     * views may not be duplicates of already registered ones.
     *
     * @param view the view to register. may not be `null`, may not be bound, must
     * 			be packed and may not be a duplicate of an already registered view
     * @throws NullPointerException if `view` is `null`
     * @throws IllegalArgumentException if `view` is bound, unpacked or a duplicate
     */
    @throws[NullPointerException]
    @throws[IllegalArgumentException]
    override def register(view: View[T]) =
        if (view == null) throw new NullPointerException
        else if (view.isBound) throw new IllegalArgumentException("view may not be bound")
        else if (!view.isPacked) throw new IllegalArgumentException("view must be packed")
        else if (views contains view) throw new IllegalArgumentException("duplicate view")
        else views :+= view

    /**
     * update the views from the data structure
     *
     * @see [[View]] for full documentation
     * @note thread-safe implementation
     *
     * @param u the data structure to update from. may not be `null`
     * @return a list with all assigned views in the order of the view stack
     * @throws NullPointerException if `u` is `null`
     * @throws IllegalStateException if no view was found to be used for the top-level view stack entry
     */
    @throws[NullPointerException]
    @throws[IllegalArgumentException]
    override def update(u: UIUniverse[T]): List[View[T]] = LOCK synchronized {

            @tailrec
            def assign(items: List[ViewData], views: List[View[T]], acc: List[(ViewData, View[T])]): List[(ViewData, View[T])] =
                if (!items.isEmpty)
                    views.find(_.isApplicable(items.head)) match {
                        case Some(v) => assign(items.tail, views.filterNot(_ equals v), (items.head, v) :: acc)
                        case None    => assign(items.tail, views, (items.head, null) :: acc)
                    }
                else
                    acc.reverse

        if (u == null)
            throw new NullPointerException
        val assignments = assign(u.ui.viewStack, views, Nil)
        if (assignments.isEmpty)
            return Nil
        if (assignments.head._2 == null)
            throw new IllegalStateException("view stack head must always be assignable to a view")
        val assignedViews = assignments.map(_._2)
        views.filterNot(assignedViews contains _).foreach(x => {
            if (!x.isPacked) x.pack
            if (x.isBound) x.unbind
        })
        assignedViews.tail.foreach(x => {
            if (!x.isPacked) x.pack
        })
        assignedViews.headOption.filter(_.isPacked).foreach(_.unpack)
        assignments.foreach {
            case (_, null) => // drop it
            case (d, v) =>
                if (!v.isBound) v bind new ViewCommandExecutor(executor, v)
                v update (u.data, d, u.ui.props)
        }
        assignedViews
    }

}