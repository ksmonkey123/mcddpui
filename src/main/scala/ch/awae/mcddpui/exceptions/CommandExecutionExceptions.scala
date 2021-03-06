package ch.awae.mcddpui.exceptions

/**
 * an exception that occurred while executing a [[ch.waan.mcddpui.api.Command Command]].
 * This exception is always backed by a Throwable.
 *
 * @author Andreas Waelchli <andreas.waelchli@me.com>
 * @version 1.2 (0.2.0), 2016-03-01
 * @since MCDDPUI 0.1.0
 *
 * @constructor creates a new instance
 * @param cause the throwable that resulted in the exception. Is recommended not to be `null`.
 */
class CommandExecutionException(cause: Throwable) extends MCDDPUIException(cause = cause)

/**
 * an exception that occurred while executing a [[ch.waan.mcddpui.api.ReadCommand ReadCommand]].
 * This exception is always backed by a Throwable.
 *
 * @author Andreas Waelchli <andreas.waelchli@me.com>
 * @version 1.1 (0.1.0), 2016-02-29
 * @since MCDDPUI 0.1.0
 *
 * @constructor creates a new instance
 * @param cause the throwable that resulted in the exception. Is recommended not to be `null`.
 */
case class ReadCommandExecutionException(cause: Throwable) extends CommandExecutionException(cause)

/**
 * an exception that occurred while executing a [[ch.waan.mcddpui.api.MutationCommand MutationCommand]].
 * This exception is always backed by a Throwable.
 *
 * @author Andreas Waelchli <andreas.waelchli@me.com>
 * @version 1.1 (0.1.0), 2016-02-29
 * @since MCDDPUI 0.1.0
 *
 * @constructor creates a new instance
 * @param cause the throwable that resulted in the exception. Is recommended not to be `null`.
 */
case class MutationCommandExecutionException(cause: Throwable) extends CommandExecutionException(cause)

/**
 * an exception that occurred while executing a [[ch.waan.mcddpui.api.ManagerCommand ManagerCommand]].
 * This exception is always backed by a Throwable.
 *
 * @author Andreas Waelchli <andreas.waelchli@me.com>
 * @version 1.1 (0.1.0), 2016-02-29
 * @since MCDDPUI 0.1.0
 *
 * @constructor creates a new instance
 * @param cause the throwable that resulted in the exception. Is recommended not to be `null`.
 */
case class ManagerCommandExecutionException(cause: Throwable) extends CommandExecutionException(cause)