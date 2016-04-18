package ch.waan.mcddpui.ui

import java.awt.event.FocusAdapter
import java.awt.event.FocusEvent

import ch.waan.mcddpui.api.CommandExecutor
import ch.waan.mcddpui.api.MutationCommand
import javax.swing.JTextField

class AutoCommittingTextField[T](text: String, cols: Int, ƒ: String => MutationCommand[T, T])(implicit executor: () => CommandExecutor[T]) extends JTextField(text, cols) {

    private var cache = text

    override def setText(s: String) = {
        cache = s
        super.setText(s)
    }

    addFocusListener(new FocusAdapter {

        override def focusLost(e: FocusEvent) = {
            val t = getText
            if (t != cache) {
                cache = t
                executor()(ƒ(t))
            }
        }

    })

}