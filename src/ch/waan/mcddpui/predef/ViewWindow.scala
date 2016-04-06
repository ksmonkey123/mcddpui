package ch.waan.mcddpui.predef

import java.util.UUID

/**
 * common trait for all window (or window-like) types
 * designed for holding [[View Views]].
 * 
 * @author Andreas Waelchli <andreas.waelchli@me.com>
 * @version 1.1 (0.3.1), 2016-04-06
 * @since MCDDPUI 0.3.1
 */
trait ViewWindow {

    /**
     * requests the window to display the view identified
     * by the given `uuid`
     * 
     * @param uuid the id of the requested view
     */
    def showView(uuid: UUID): Unit

}