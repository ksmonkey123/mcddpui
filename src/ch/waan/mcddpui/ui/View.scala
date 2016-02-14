package ch.waan.mcddpui.ui

import ch.waan.mcddpui.structure.ViewData
import javax.swing.JComponent
import ch.waan.mcddpui.structure.UIUniverse

trait View[T, -U, V <: ViewData] {
  def update(data: T, viewData: V): Option[Throwable]
  def component: JComponent
  def updater: PartialFunction[(U, ViewData), Option[Throwable]]
}