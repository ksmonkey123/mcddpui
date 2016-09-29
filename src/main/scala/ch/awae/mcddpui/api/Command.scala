package ch.awae.mcddpui.api

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
 * @version 1.3 (0.2.0), 2016-03-02
 * @since MCDDPUI 0.1.0
 */
object Command {

    /**
     * creates a [[MutationCommand]] with a defined name - compatible to java lambdas.
     *
     * @example
     * {{{
     * // Java: use with lambda
     * MutationCommand<String, String> c = Command.get(s -> s.toUpperCase(), "toUpperCase");
     * }}}
     *
     * @tparam T the input type of the command
     * @tparam U the output type of the command
     * @param f the [[MutationFunction]] instance to be converted to
     * 				a [[MutationCommand]]. That trait is compatible
     * 				with java lambdas. May not be `null`.
     * @param name the name of the command. May be `null`.
     * @return a [[MutationCommand]] based off the the function `f`
     * @throws NullPointerException if `f` is `null`
     */
    def get[T, U](f: MutationFunction[T, U], name: String): MutationCommand[T, U] =
        if (f == null)
            throw new NullPointerException
        else
            new LambdaMutationCommand(f, name)

    /**
     * creates a nameless [[MutationCommand]] - compatible to java lambdas.
     *
     * equivalent to `Command.get(f, null)`
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
    def get[T, U](f: MutationFunction[T, U]): MutationCommand[T, U] = get(f, null)

    /**
     * the instance of the UndoCommand singleton
     *
     * @note for scala use [[UndoCommand]] directly
     */
    def undoCommand = UndoCommand

    /**
     * the instance of the RedoCommand singleton
     *
     * @note for scala use [[RedoCommand]] directly
     *
     * @param index the redo path index
     */
    def redoCommand(index: Int) = RedoCommand(index)

}

/**
 * Functional Interface variant of the [[MutationCommand]] trait.
 *
 * {{{
 * // Java: use implicitly as lambda with name
 * MutationCommand<String, String> c0 = Command.get(s -> s.toUpperCase(), "toUpperCase");
 * // Java: use implicitly as lambda without name. Name will be null by default
 * MutationCommand<String, String> c1 = Command.get(s -> s.toUpperCase());
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
 * Marker Trait for commands.
 *
 * @author Andreas Waelchli <andreas.waelchli@me.com>
 * @version 1.1 (0.2.0), 2016-03-01
 * @since MCDDPUI 0.2.0
 */
trait CommandLike