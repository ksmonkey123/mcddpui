package ch.waan.mcddpui.exceptions

import scala.util.control.NonFatal

/**
 * an exception that occurred while manipulating the history of a
 * [[ch.waan.mcddpui.Record Record]]
 *
 * @author Andreas Waelchli <andreas.waelchli@me.com>
 * @version 1.2 (0.2.0), 2016-03-01
 * @since MCDDPUI 0.1.0
 *
 * @constructor creates a new exception instance
 * @param msg a message describing the exception. May be `null`.
 */
case class RecordHistoryManipulationException(msg: String) extends MCDDPUIException(msg = msg)
