package ch.waan.mcddpui.api

import ch.waan.mcddpui.exceptions.ViewUpdateException
import scala.collection.immutable.HashMap

/**
 * base trait for all views.
 *
 * This trait does not discriminate between multi-frame and single-view UI solutions.
 *
 * == View Life-Cycle ==
 * A view be either '''unbound''' or '''bound''' and '''unpacked''' or '''packed'''.
 *
 * The ''binding state'' of a view describes whether or not a view is ''bound'' to a
 * [[CommandExecutor]]. Whenever a view is unbound, no reference to an executor may be
 * retained and no commands may be dispatched from the view. Whenever a view is bound,
 * it must retain a reference to exactly one executor. This is the executor the view may
 * dispatch commands to. As long as a view is bound, it may dispatch commands.
 *
 * The ''packing state'' of a view describes whether or not a view is ''active''.
 * An active (unpacked) view must be visible and focused. Interactions with the view
 * are allowed, and the view must be bound. At any time exactly one view may be active.
 * An inactive (packed) view may be visible or invisible, however no direct interaction
 * with the view is allowed. An inactive view may therefore never dispatch any commands
 * - even when bound. There is however one exception to this rule:
 *  - Since only one view can be active at once, unfocused frames in a multi-frame UI are
 *      packed. A view in an unfocused frame may request focus when clicked on. This is
 *      necessary to be able to keep the data structure synchronized with the UI. For this
 *      use the command [[TargetFocusRequestCommand]] is provided. This is the only command
 *      that may be issued from an inactive view.
 *
 * === Transitions ===
 *  1. When starting the application, all views should be constructed and initialized
 * 			as far as possible. After startup all views should be '''packed''' and
 * 			'''unbound'''
 * 	1. When a view enters the view stack, it is '''bound''' but remains '''packed'''
 *  1. When a view reaches the top of the stack, it is '''bound''' and '''unpacked'''
 *  1. When a view leaves the top of the stack, it is '''bound''' and '''packed'''
 *  1. When a view leaves the view stack, it is '''unbound''' and '''packed'''
 *
 * === Updates ===
 * During UI updates the views are updated in the order they appear in the view stack. If a
 * view is applicable to multiple entries in the view stack, the top-most one will be used.
 * If multiple view are applicable to the same entry, the first matching view is used. The
 * views are checked in the order they were registered.
 *
 * The update cycle operates according to a simple procedure:
 *  1. Try to assign a view to every entry in the view stack. Entries near the head of the
 *  		view stack are prioritized, and views are prioritized in the order they were registered.
 *  1. Assert that the top-most entry of the view stack has a view assigned.
 *  1. Pack the view that was previously assigned to the top-most entry of the stack if it
 *  		it is not again assigned to the top-most element.
 *  1. Unbind all views that were assigned previously, that have no (new assignment).
 *  1. Update all assigned views.
 *  1. Bind all views that previously had no assignment.
 *  1. Unpack the view assigned to the top-most element if it is packed.
 *
 *  @author Andreas Waelchli <andreas.waelchli@me.com>
 *  @version 1.1 (0.2.0), 2016-03-02
 *  @since MCDDPUI 0.2.0
 *
 *  @tparam T the data type of the data structure
 */
trait View[T] {

    /**
     * binds the view to a [[CommandExecutor]]
     *
     * this method may only be called when the view is '''unbound'''
     *
     * @param executor the executor to bind the view to. may not be `null`
     * @throws NullPointerException if the `executor` is `null`
     * @throws IllegalStateException if the view is already '''bound'''
     */
    @throws[NullPointerException]
    @throws[IllegalStateException]
    def bind(executor: CommandExecutor[UIUniverse[T]]): Unit

    /**
     * indicates whether the view is currently bound or unbound
     */
    def isBound: Boolean

    /**
     * unbinds the view from the currently bound [[CommandExecutor]]
     *
     * this method may only be called when the view is '''bound'''
     *
     * @throws IllegalStateException if the view is already '''unbound'''
     */
    @throws[IllegalStateException]
    def unbind: Unit

    /**
     * unpacks the view
     *
     * this method may only be called when the view is '''packed'''
     *
     * @throws IllegalStateException if the view is already '''unpacked'''
     */
    @throws[IllegalStateException]
    def unpack: Unit

    /**
     * indicates whether the view is currently packed or unpacked
     */
    def isPacked: Boolean

    /**
     * packs the view
     *
     * this method may only be called when the view is '''unpacked'''
     *
     * @throws IllegalStateException if the view is already '''packed'''
     */
    @throws[IllegalStateException]
    def pack: Unit

    /**
     * checks if the view is applicable to the given [[ViewData]] instance
     * 
     * This is used to assign views to elements in the view stack.
     * 
     * @param v the [[ViewData]] instance to check the applicability for
     * @return `true` if this view is applicable to the given `v`. `false` otherwise
     */
    def isApplicable(v: ViewData): Boolean

    /**
     * updates the view
     * 
     * @param data the ''operational data'' to update from
     * @param viewData the [[ViewData]] instance the view is assigned to
     * @param props the global UI property hash map
     * @throws ViewUpdateException
     */
    @throws[ViewUpdateException]
    def update(data: T, viewData: ViewData, props: HashMap[String, String]): Unit

}