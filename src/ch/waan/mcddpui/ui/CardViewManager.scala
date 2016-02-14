package ch.waan.mcddpui.ui

import java.awt.CardLayout
import scala.util.Try
import javax.swing.JPanel
import ch.waan.mcddpui.structure.RootViewData
import ch.waan.mcddpui.structure.ViewData

class CardViewManager[T](rootView: View[_, T, RootViewData], views: View[_, T, _ <: ViewData]*) extends ViewManager[T] {

  val panel = new JPanel(new CardLayout)

  registerView(rootView).foreach(throw _)
  views foreach (x => registerView(x) foreach (throw _))

  protected def addView(view: View[_, T, _ <: ViewData], id: String): Option[Throwable] = Try {
    panel.add(view.component, id)
  }.failed.toOption

  protected def showView(id: String): Option[Throwable] = Try {
    panel.getLayout.asInstanceOf[CardLayout].show(panel, id)
  }.failed.toOption

}