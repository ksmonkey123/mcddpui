package ch.waan.mcddpui.exceptions

import scala.util.control.NonFatal

/**
 * an exception that occurred while manipulating the history of a
 * [[ch.waan.mcddpui.Record]]
 *
 * @author Andreas Waelchli <andreas.waelchli@me.com>
 * @version 1.1 (0.1.0), 2016-02-29
 * @since MCDDPUI 0.1.0
 *
 * @constructor creates a new exception instance
 * @param msg a message describing the exception. May be `null`.
 */
case class RecordHistoryManipulationException(msg: String) extends Exception(msg)
