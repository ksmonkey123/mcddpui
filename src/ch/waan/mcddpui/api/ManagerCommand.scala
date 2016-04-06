package ch.waan.mcddpui.api

/**
 * A manager command is a command targeted at a [[CommandExecutor]]
 * to influence the internal behaviour of the executor.
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
 *
 * @param index the index of the desired redo path
 */
case class RedoCommand(index: Int) extends ManagerCommand

/**
 * a command requesting focus for a given view
 *
 * This command should only be used in multi-frame applications and should
 * only be dispatched from the view representing the target [[ViewData]].
 *
 * @author Andreas Waelchli <andreas.waelchli@me.com>
 * @version 1.1 (0.2.0), 2016-03-02
 * @since MCDDPUI 0.2.0
 *
 * @param the target [[ViewData]]
 */
case class TargetFocusRequestCommand(v: ViewData) extends ManagerCommand