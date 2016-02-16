package ch.waan.mcddpui.ui

import ch.waan.mcddpui.structure.ViewData
import javax.swing.JComponent
import ch.waan.mcddpui.structure.UIUniverse

trait View[-U] {
  def component: JComponent
  def updater: PartialFunction[(U, ViewData), Option[Throwable]]
}