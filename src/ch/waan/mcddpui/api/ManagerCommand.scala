package ch.waan.mcddpui.api

/**
 * A manager command is a command targeted at a [[CommandExecutor]]
 * to influence the internal behavior of the executor.
 *
 * They are not targeted at modification of the internal data structure.
 * In fact this trait is a pure marker trait without any methods.
 *
 * @author Andreas Waelchli <andreas.waelchli@me.com>
 * @version 1.2 (0.2.0), 2016-03-01
 * @since MCDDPUI 0.1.0
 */
trait ManagerCommand extends CommandLike

/**
 * Collection of predefined manager commands.
 *
 * @author Andreas Waelchli <andreas.waelchli@me.com>
 * @version 1.2 (0.2.0), 2016-03-01
 * @since MCDDPUI 0.1.0
 */
@deprecated("use commands directly", "MCDDPUI 0.2.0")
object ManagerCommand {

    /**
     * a command requesting the executor to undo a mutation
     */
    @deprecated("use ch.waan.mcddpui.api.UndoCommand", "MCDDPUI 0.2.0")
    def UndoCommand = ch.waan.mcddpui.api.UndoCommand

    /**
     * a command requesting the executor to redo a mutation
     */
    @deprecated("use ch.waan.mcddpui.api.RedoCommand", "MCDDPUI 0.2.0")
    def RedoCommand = ch.waan.mcddpui.api.RedoCommand
}

/**
 * a command requesting the executor to undo a mutation
 *
 * @author Andreas Waelchli <andreas.waelchli@me.com>
 * @version 1.1 (0.2.0), 2016-03-01
 * @since MCDDPUI 0.2.0
 */
case object UndoCommand extends ManagerCommand

/**
 * a command requesting the executor to redo a mutation
 *
 * @author Andreas Waelchli <andreas.waelchli@me.com>
 * @version 1.1 (0.2.0), 2016-03-01
 * @since MCDDPUI 0.2.0
 */
case object RedoCommand extends ManagerCommand
