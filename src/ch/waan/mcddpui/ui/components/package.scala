package ch.waan.mcddpui.ui

package object components {

  @inline
  implicit def commandButton2JButton(b: CommandButton[_]) = b.button

}