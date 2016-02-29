package ch.waan.mcddpui.exceptions

import scala.util.control.NonFatal

/**
 * Utilities for exception handling
 *
 * @author	Andreas Waelchli <andreas.waelchli@me.com>
 * @version	1.1 (0.1.0), 2016-02-29
 * @since	MCDDPUI 0.1.0
 */
object ExceptionUtils {

    /**
     * throws any fatal Throwable, while returning the rest.
     *
     * @note the implementation is based off
     * 			`scala.util.control.NonFatal` to distinguish
     * 			between fatal and non-fatal exceptions
     *
     * @since MCDDPUI 0.1.0
     *
     * @param th the Throwable to process
     * @return the Throwable `th` if it is considered non-fatal
     * @throws Throwable if the throwable is considered fatal
     */
    def throwFatal(th: Throwable) = th match {
        case NonFatal(t) => t
        case t           => throw t
    }

}