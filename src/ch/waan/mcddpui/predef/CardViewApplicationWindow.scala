package ch.waan.mcddpui.predef

import ch.waan.mcddpui.api.Record
import ch.waan.mcddpui.api.UIUniverse
import ch.waan.mcddpui.exceptions.CommandExecutionException
import javax.swing.JFrame
import javax.swing.JToolBar
import javax.swing.JPanel
import java.awt.CardLayout
import java.awt.BorderLayout
import java.util.UUID
import ch.waan.mcddpui.api._

abstract class CardViewApplicationWindow[T](record: Record[UIUniverse[T]], titleProperty: String = "defaults.window.title", defaultTitle: String = "null")
        extends ViewManagingCommandExecutor(record) with ViewWindow[T] {

    val frame = new JFrame()
    val toolbar = new JToolBar("Toolbar")
    val cards = new JPanel(new CardLayout())

    def init(views: PanelView[T]*): Unit = {
        frame setLayout new BorderLayout()
        frame.add(toolbar, BorderLayout.PAGE_START)
        frame.add(cards, BorderLayout.CENTER)
        views foreach registerView
        initToolbar
        frame.pack
        frame setVisible true
        record.view((x: UIUniverse[T]) => {
            update(x)
            return
        })
    }

    override def customUpdate(u: UIUniverse[T]): Unit = {
        val head = u.ui.viewStack.head
        if (head.isInstanceOf[TitledViewData])
            frame.setTitle(head.asInstanceOf[TitledViewData].title)
        else if (u.ui.props.contains(titleProperty))
            frame.setTitle(u.ui.props(titleProperty))
        else
            frame.setTitle(defaultTitle)
    }

    def initToolbar

    def registerView(view: PanelView[T]): Unit = {
        register(view)
        cards.add(view.component, view.uuid.toString)
        frame.pack
    }

    def showView(uuid: UUID): Unit = {
        cards.getLayout.asInstanceOf[CardLayout].show(cards, uuid.toString)
        cards.repaint()
    }

}