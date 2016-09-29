package ch.awae.mcddpui.ui

import java.awt.event.ActionEvent

import javax.swing.AbstractAction
import javax.swing.Action
import scala.language.implicitConversions

class FunctionalAction(_text: String, _desc: String, ƒ: ActionEvent => Unit) extends AbstractAction(_text) {

  putValue(Action.SHORT_DESCRIPTION, _desc)

  def text_=(s: String): Unit = putValue(Action.NAME, s)
  def text: String = getValue(Action.NAME).asInstanceOf[String]

  def desc_=(s: String): Unit = putValue(Action.SHORT_DESCRIPTION, s)
  def desc: String = getValue(Action.SHORT_DESCRIPTION).asInstanceOf[String]

  override def actionPerformed(e: ActionEvent) = ƒ(e)

}

object FunctionalAction {

  object Implicit {
    implicit def bodyToFunction[T](body: => Unit): T => Unit = _ => body
  }

  def apply(text: String, desc: String)(ƒ: ActionEvent => Unit) = new FunctionalAction(text, desc, ƒ)

}