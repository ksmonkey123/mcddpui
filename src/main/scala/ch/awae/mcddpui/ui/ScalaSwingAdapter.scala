package ch.awae.mcddpui.ui

import javax.swing.JComponent
import javax.swing.text.JTextComponent

object ScalaSwingAdapter {

    implicit class ScalaJComponent(val component: JComponent) extends AnyVal {
        def visible = component.isVisible()
        def visible_=(visible: Boolean) = component.setVisible(visible)
        def enabled = component.isEnabled()
        def enabled_=(enabled: Boolean) = component.setEnabled(enabled)
        def tooltip = component.getToolTipText()
        def tooltip_=(tooltip: String) = component.setToolTipText(tooltip)
    }

    implicit class ScalaJTextComponent(val component: JTextComponent) extends AnyVal {
        def text = component.getText
        def text_=(text: String) = component.setText(text)
    }

}