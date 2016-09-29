package ch.awae.mcddpui.exceptions

/**
 * common base class for all custom exceptions of the MCDDPUI library
 *
 * @author Andreas Waelchli <andreas.waelchli@me.com>
 * @version 1.1 (0.2.0), 2016-03-01
 * @since MCDDPUI 0.2.0
 *
 * @constructor creates a new exception instance
 * @param msg a message. may be `null`.
 * @param cause a throwable that resulted in this exception. may be `null`.
 */
class MCDDPUIException(msg: String = null, cause: Throwable = null) extends Exception(msg, cause)