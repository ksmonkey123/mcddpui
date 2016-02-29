package ch.waan.mcddpui.api

/**
 * Command Helper
 *
 * This object contains command getters useful for command
 * instantiation through java lambda definitions. Also getters
 * for the undo and redo commands are provided that map back
 * to the corresponding singleton objects
 *
 * @note this object is designed for usage with java
 *
 * @author Andreas Waelchli <andreas.waelchli@me.com>
 * @version 1.1 (0.1.0), 2016-02-26
 * @since MCDDPUI 0.1.0
 */
object Command {

    /**
     * Functional Interface variant of the [[MutationCommand]] trait.
     *
     * {{{
     * // Java: use implicitly as lambda
     * MutationCommand<String, String> c = Command.get(s -> s.toUpperCase());
     * }}}
     *
     * @version 1.1 (0.1.0), 2016-02-26
     * @since MCDDPUI 0.1.0
     */
    @FunctionalInterface
    trait MutationFunction[T, U] {
        /**
         * @see [[MutationCommand.apply]]
         */
        @throws(classOf[Throwable])
        def apply(t: T): U
    }

    /**
     * creates a [[MutationCommand]] - compatible to java lambdas.
     *
     * @example
     * {{{
     * // Java: use with lambda
     * MutationCommand<String, String> c = Command.get(s -> s.toUpperCase());
     * }}}
     *
     * @tparam T the input type of the command
     * @tparam U the output type of the command
     * @param f the [[MutationFunction]] instance to be converted to
     * 				a [[MutationCommand]]. That trait is compatible
     * 				with java lambdas. May not be `null`.
     * @return a [[MutationCommand]] based off the the function `f`
     * @throws NullPointerException if `f` is `null`
     */
    def get[T, U](f: MutationFunction[T, U]): MutationCommand[T, U] =
        if (f == null)
            throw new NullPointerException
        else
            new MutationCommand[T, U] {
                override def apply(t: T) = f(t)
            }

    /**
     * the instance of the UndoCommand singleton
     *
     * @note for scala use [[ManagerCommand.UndoCommand]] directly
     */
    def undoCommand = ManagerCommand.UndoCommand

    /**
     * the instance of the RedoCommand singleton
     *
     * @note for scala use [[ManagerCommand.RedoCommand]] directly
     */
    def redoCommand = ManagerCommand.RedoCommand

}

/**
 * Marker Trait for commands.
 * 
 * @author Andreas Waelchli <andreas.waelchli@me.com>
 * @version 1.1 (0.1.0), 2016-02-29
 * @since MCDDPUI 0.1.0
 */
trait Command;