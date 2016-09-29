package ch.awae.mcddpui.ui

import FunctionalAction.Implicit.bodyToFunction
import ch.awae.mcddpui.api.CommandExecutor
import ch.awae.mcddpui.api.ManagerCommand
import ch.awae.mcddpui.api.MutationCommand
import ch.awae.mcddpui.api.ReadCommand
import ch.awae.mcddpui.api.functionNameTuple2mutationCommand
import ch.awae.mcddpui.api.mutationCommand2function
import ch.awae.mcddpui.exceptions.ManagerCommandExecutionException
import ch.awae.mcddpui.exceptions.MutationCommandExecutionException
import ch.awae.mcddpui.exceptions.ReadCommandExecutionException

object CommandAction {

    def apply(name: String, desc: String, cmd: ManagerCommand)(implicit ex: () => CommandExecutor[_]) =
        FunctionalAction(name, desc)(ex()(cmd))

    def apply[T](name: String, desc: String, cmd: ReadCommand[T])(implicit ex: () => CommandExecutor[T]) =
        FunctionalAction(name, desc)(ex()(cmd))

    def apply[T](name: String, desc: String, cmd: MutationCommand[T, T])(implicit ex: () => CommandExecutor[T]): FunctionalAction =
        if (cmd.name == null)
            apply(name, desc, (cmd: T => T, desc))
        else
            FunctionalAction(name, desc)(ex()(cmd))

}