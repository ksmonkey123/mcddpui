package ch.waan.mcddpui.ui

import java.awt.CardLayout
import scala.util.Try
import javax.swing.JPanel
import ch.waan.mcddpui.structure.RootViewData
import ch.waan.mcddpui.structure.ViewData
import scala.annotation.varargs

class CardViewManager[T](rootView: View[T], views: List[View[T]]) extends ViewManager[T] {

  def this(root: View[T]) = this(root, List.empty)
  def this(root: View[T], views: View[T]*) = this(root, views.toList)

  val panel = new JPanel(new CardLayout)

  registerView(rootView).foreach(throw _)
  views foreach (x => registerView(x) foreach (throw _))

  protected def addView(view: View[T], id: String): Option[Throwable] = Try {
    panel.add(view.component, id)
  }.failed.toOption

  protected def showView(id: String): Option[Throwable] = Try {
    panel.getLayout.asInstanceOf[CardLayout].show(panel, id)
  }.failed.toOption

}