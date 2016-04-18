package ch.waan.ksprec.ui

import FunctionalAction.Implicit.bodyToFunction
import ch.waan.mcddpui.api.CommandExecutor
import ch.waan.mcddpui.api.ManagerCommand
import ch.waan.mcddpui.api.MutationCommand
import ch.waan.mcddpui.api.ReadCommand
import ch.waan.mcddpui.api.functionNameTuple2mutationCommand
import ch.waan.mcddpui.api.mutationCommand2function

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