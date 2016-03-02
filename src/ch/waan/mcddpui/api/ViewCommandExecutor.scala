package ch.waan.mcddpui.api

import java.util.Objects

/**
 * command executor for view interaction that checks if a view is
 * in a legal state for sending commands.
 *
 * @author Andreas Waelchli <andreas.waelchli@me.com>
 * @version 1.1 (0.2.0), 2016-03-02
 * @since MCDDPUI 0.2.0
 *
 * @constructor creates a new command executor
 * @param backer the command executor that actually does the work. may not be `null`.
 * @param view the view to monitor. may not be `null`.
 * @throws NullPointerException if `backer` or `view` is `null`
 *
 * @see [[View]] for complete documentation
 */
@throws[NullPointerException]
final class ViewCommandExecutor[T](backer: CommandExecutor[UIUniverse[T]], view: View[T])
        extends CommandExecutor[UIUniverse[T]] {
    Objects.requireNonNull(backer)
    Objects.requireNonNull(view)

    private def checkBound = if (!view.isBound)
        throw new IllegalStateException("unbound but still sending commands?")
    private def checkUnpacked = if (view.isPacked)
        throw new IllegalStateException("packed but still sending commands?")

    override def apply(c: ManagerCommand) = {
        this.checkBound
        if (!c.isInstanceOf[TargetFocusRequestCommand])
            this.checkUnpacked
        backer(c)
    }

    override def apply(c: ReadCommand[_ >: UIUniverse[T]]) = {
        this.checkBound
        this.checkUnpacked
        backer(c)
    }

    override def apply(c: MutationCommand[_ >: UIUniverse[T], _ <: UIUniverse[T]]) = {
        this.checkBound
        this.checkUnpacked
        backer(c)
    }

}