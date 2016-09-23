package ch.waan.mcddpui.predef

import ch.waan.mcddpui.api.ViewData
import ch.waan.mcddpui.api.UIUniverse

/**
 * common base trait for all [[ViewData]] instances with a title.
 *
 * @note this trait is designed for integration with [[CardViewApplicationWindow]] to allow
 * automatic updates of the window title based on the current view.
 *
 * @see [[ViewData]]
 *
 * @author Andreas Waelchli <andreas.waelchli@me.com>
 * @version 1.1 (0.3.1), 2016-04-06
 * @since MCDDPUI 0.3.1
 */
trait TitledViewData extends ViewData {

    def title: String

}

trait DynamicallyTitledViewData[Model] extends ViewData {

    def title(u: UIUniverse[Model]): String

}