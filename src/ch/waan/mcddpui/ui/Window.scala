package ch.waan.mcddpui.ui

import ch.waan.mcddpui.structure.Command
import ch.waan.mcddpui.structure.CommandDispatcher
import ch.waan.mcddpui.structure.UIUniverse
import ch.waan.mcddpui.structure.UIUniverse
import ch.waan.mcddpui.structure.DataManager
import ch.waan.mcddpui.structure.Record

abstract class Window[T](
  protected val record: Record[UIUniverse[T]],
  viewManager: ViewManager[T])
    extends DataManager[UIUniverse[T]] {

  if (viewManager == null)
    throw new NullPointerException("[null] viewManager not allowed")
  if (record == null)
    throw new NullPointerException("[null] record not allowed")

  protected def updateWindow(u: UIUniverse[T]): Option[Throwable]
  override protected def refresh(u: UIUniverse[T]): Option[Throwable] = {
    viewManager.updateView(u) orElse updateWindow(u)
  }
}