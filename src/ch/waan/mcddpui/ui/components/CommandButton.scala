package ch.waan.mcddpui.ui.components

import java.awt.event.ActionEvent
import java.awt.event.ActionListener

import scala.util.Try

import ch.waan.mcddpui.structure.Command
import ch.waan.mcddpui.structure.CommandDispatcher
import ch.waan.mcddpui.structure.ErrorCommand
import ch.waan.mcddpui.structure.UIUniverse
import javax.swing.Icon
import javax.swing.JButton


class CommandButton[T](
    text: String,
    icon: Icon,
    tooltip: String,
    command: () => Try[Command[_ >: UIUniverse[T], _ <: UIUniverse[T]]])(implicit d: CommandDispatcher[UIUniverse[T]]) {

  if (command == null) throw new NullPointerException("command may not be [null]")

  val button = new JButton(text, icon)
  button.setToolTipText(tooltip)
  button.addActionListener(new ActionListener {
    override def actionPerformed(e: ActionEvent): Unit =
      command().recover { case x => ErrorCommand(x) }.map(_.dispatch(d))
  })

  def enable = button.setEnabled(true)
  def disable = button.setEnabled(false)
  def inject(f: JButton => Unit) = Try(f(button)).failed.toOption

}

object CommandButton {

  def apply[T](text: String, icon: Icon, tooltip: String, command: () => Try[Command[_ >: UIUniverse[T], _ <: UIUniverse[T]]])(implicit d: CommandDispatcher[UIUniverse[T]]) =
    new CommandButton(text, icon, tooltip, command)

}