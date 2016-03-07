package ch.waan.mcddpui.api

import ch.waan.mcddpui.exceptions.ReadCommandExecutionException
import ch.waan.mcddpui.exceptions.MutationCommandExecutionException
import ch.waan.mcddpui.exceptions.ManagerCommandExecutionException

/**
 * A handler that accepts commands and executes them.
 *
 * @author Andreas Waelchli <andreas.waelchli@me.com>
 * @version 1.1 (0.1.0), 2016-02-26
 * @since MCDDPUI 0.1.0
 *
 * @tparam T the type of the data structure the executor
 * 					operates on
 *
 * @define NULLMSG The implementation may decide whether or not `null`
 * 					commands should be allowed. If `null` commands are
 * 					not allowed by the implementation, any invocation
 * 					with a `null` command should result in a
 * 					NullPointerException. If however an implementation
 * 					decides to allow `null` commands, such commands
 * 					should be ignored. It is recommended to ensure
 * 					consistent `null` handling over all methods.
 *
 * @define NPEMSG if the implementation chooses to disallow `null`
 * 					commands.
 *
 * @define EXMSG if any exceptional case occurs while executing the command.
 */
trait CommandExecutor[T] {

    /**
     * executes a [[ReadCommand]]
     *
     * @note $NULLMSG
     * @param c the [[ReadCommand]] to execute.
     * @throws NullPointerException $NPEMSG
     * @throws ReadCommandExecutionException $EXMSG
     */
    @throws(classOf[ReadCommandExecutionException])
    def apply(c: ReadCommand[_ >: T]): Unit

    /**
     * executes a [[MutationCommand]]
     *
     * @note $NULLMSG
     * @param c the [[MutationCommand]] to execute.
     * @throws NullPointerException $NPEMSG
     * @throws MutationCommandExecutionException $EXMSG
     */
    @throws(classOf[MutationCommandExecutionException])
    def apply(c: MutationCommand[_ >: T, _ <: T]): Unit

    /**
     * executes a [[ManagerCommand]]
     *
     * @note $NULLMSG
     * @param c the [[ManagerCommand]] to execute.
     * @throws NullPointerException $NPEMSG
     * @throws ManagerCommandExecutionException $EXMSG
     */
    @throws(classOf[ManagerCommandExecutionException])
    def apply(c: ManagerCommand): Unit

}