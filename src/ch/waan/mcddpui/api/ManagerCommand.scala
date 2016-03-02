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
