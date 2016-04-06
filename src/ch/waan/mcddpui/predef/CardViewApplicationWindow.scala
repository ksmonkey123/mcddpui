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

/**
 * A card-based [[ViewWindow]] with integrated command execution and view management designed
 * for single-frame applications.
 *
 * The window title is taken from the [[ViewData]] instance on top of the view stack. If it
 * does not provide a title itself (i.e. if it does not implement [[TitledViewData]]), a
 * default title is used instead.
 *
 * The window is based off the swing CardLayout and is packed to the largest registered view.
 * Therefore the window will always have the minimal size required for fully accommodating
 * the largest view in its packed size.
 *
 * The window also provides support for a toolbar and a menu bar with full OS layout rule
 * handling. (TODO)
 *
 * @author Andreas Waelchli <andreas.waelchli@me.com>
 * @version 1.1 (0.3.1), 2016-04-06
 * @since MCDDPUI 0.3.1
 *
 * @tparam the model data type of the data structure
 *
 * @param record a record holding the data structure
 * @param defaultTitle a default title to be used if the current view does not support one.
 */
abstract class CardViewApplicationWindow[T](record: Record[UIUniverse[T]], defaultTitle: String = "null")
        extends ViewManagingCommandExecutor(record) with ViewWindow {

    val frame = new JFrame()
    val toolbar = new JToolBar("Toolbar")
    val cards = new JPanel(new CardLayout())

    /**
     * Initialise the window with an arbitrary number of views
     */
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

    /**
     * updates the window when the universe updates.
     * The default implementation handles updating the
     *
     * @note When overriding this method, a call to *super*
     * must be included to ensure correct behaviour.
     */
    override def customUpdate(u: UIUniverse[T]): Unit = {
        val head = u.ui.viewStack.head
        if (head.isInstanceOf[TitledViewData])
            frame.setTitle(head.asInstanceOf[TitledViewData].title)
        else if (u.ui.props.contains("defaults.window.title"))
            frame.setTitle(u.ui.props("defaults.window.title"))
        else
            frame.setTitle(defaultTitle)
    }

    /**
     * Initialise the toolbar
     */
    def initToolbar

    /**
     * Register a new view.
     *
     * @note After registering frame will be packed.
     */
    def registerView(view: PanelView[T]): Unit = {
        register(view)
        cards.add(view.component, view.uuid.toString)
        frame.pack
    }

    override def showView(uuid: UUID): Unit = {
        cards.getLayout.asInstanceOf[CardLayout].show(cards, uuid.toString)
        cards.repaint()
    }

}