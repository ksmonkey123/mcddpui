package ch.waan.mcddpui.predef

import java.awt.Container
import java.util.UUID
import ch.waan.mcddpui.api.CommandExecutor
import ch.waan.mcddpui.api.UIUniverse
import ch.waan.mcddpui.api.View
import java.util.Objects

abstract class PanelView[T](window: ViewWindow[T], val uuid: UUID = UUID.randomUUID()) extends View[T] {
    type Executor = CommandExecutor[UIUniverse[T]]

    Objects.requireNonNull(window)
    Objects.requireNonNull(uuid)

    def component: Container

    private[this] var bound = false
    private[this] var packed = true
    protected var executor: Executor = null

    override def isBound = bound
    override def isPacked = packed

    @throws[NullPointerException]
    @throws[IllegalStateException]
    override def bind(ex: Executor): Unit = synchronized {
        if (bound) throw new IllegalStateException
        if (ex == null) throw new NullPointerException
        executor = ex
        bound = true
    }

    @throws[IllegalStateException]
    override def unbind: Unit = synchronized {
        if (!bound) throw new IllegalStateException
        executor = null
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