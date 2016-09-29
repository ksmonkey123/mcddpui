package ch.awae.mcddpui.predef

import java.awt.Container
import java.util.UUID
import ch.awae.mcddpui.api.CommandExecutor
import ch.awae.mcddpui.api.UIUniverse
import ch.awae.mcddpui.api.View
import java.util.Objects

/**
 * Abstract [[View]] implementation for views associated with a [[ViewWindow]].
 * The base implementation handles (un)binding, (un)packing and making the view
 * visible in the window.
 *
 * A concrete implementation must provide the root UI component of the view through
 * the `component` method.
 *
 * The `executor` potentially bound to this view is marked as implicit. This is done
 * in preparation for the planned integration of prebuilt UI components with direct
 * command integration.
 *
 * @tparam T the data type of the data structure
 *
 * @param window the window this view will be registered to. The registering must be
 * 			handled externally. The provided `window` will be used when requesting
 * 			this view to be displayed. May not be `null`
 * @param uuid the unique identifier for this view. Defaults to a randomly generated
 * 			identifier. May not be `null`
 *
 * @throws NullPointerException if any parameter is `null`
 *
 * @author Andreas Waelchli <andreas.waelchli@me.com>
 * @version 1.1 (0.3.1), 2016-04-06
 * @since MCDDPUI 0.3.1
 */
abstract class PanelView[T](window: ViewWindow, val uuid: UUID = UUID.randomUUID()) extends View[T] {
    type Executor = CommandExecutor[UIUniverse[T]]

    Objects.requireNonNull(window)
    Objects.requireNonNull(uuid)

    /**
     * The root component of this view
     */
    def component: Container

    private[this] var bound = false
    private[this] var packed = true
    private[this] var exec: Executor = null

    implicit protected val executor = () => exec

    override def isBound = bound
    override def isPacked = packed

    @throws[NullPointerException]
    @throws[IllegalStateException]
    override def bind(ex: Executor): Unit = synchronized {
        if (bound) throw new IllegalStateException
        if (ex == null) throw new NullPointerException
        exec = ex
        bound = true
    }

    @throws[IllegalStateException]
    override def unbind: Unit = synchronized {
        if (!bound) throw new IllegalStateException
        exec = null
        bound = false
    }

    @throws[IllegalStateException]
    override def unpack: Unit = synchronized {
        if (!packed) throw new IllegalStateException
        packed = false
        window showView uuid
    }

    @throws[IllegalStateException]
    override def pack: Unit = synchronized {
        if (packed) throw new IllegalStateException
        packed = true
    }

}