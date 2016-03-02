package ch.waan.mcddpui

/**
 * package holding basic API traits and other core API functionality.
 *
 * Some functionality of the scala API require implicit conversions.
 * These implicits are all contained in the package object itself.
 *
 * @author Andreas Waelchli <andreas.waelchli@me.com>
 * @since MCDDPUI 0.1.0
 */
package object api {

    /**
     * converts a [[MutationFunction]] instance into a mutation command without a name.
     */
    implicit def mutatefunc2command[T, U](f: MutationFunction[T, U]): MutationCommand[T, U] =
        Command.get(f)

    /**
     * converts a scala function into a [[ReadCommand]].
     *
     * @since MCDDPUI 0.1.0
     * @tparam T the input type of the command
     * @param f the function to be converted into a [[ReadCommand]].
     * 				May not be `null`.
     * @return a [[ReadCommand]] based off the function `f`
     * @throws NullPointerException if `f` is `null`
     * @example
     * {{{
     * import ch.waan.mcddpui.api._
     * val c0: ReadCommand[String] = (s: String) => println(s)
     * val c1: ReadCommand[String] = println(_: String)
     * }}}
     */
    implicit def function2readCommand[T](f: T => Unit): ReadCommand[T] =
        if (f == null)
            throw new NullPointerException
        else
            new ReadCommand[T] {
                override def apply(t: T) = f(t)
            }

    /**
     * converts a [[ReadCommand]] into a scala function.
     *
     * @since MCDDPUI 0.1.0
     * @tparam T the input type of the command
     * @param c the command to be converted into a function.
     * 				May not be `null`.
     * @return a function based off the command `c`
     * @throws NullPointerException if `c` is `null`
     */
    implicit def readCommand2function[T](c: ReadCommand[T]): T => Unit =
        if (c == null)
            throw new NullPointerException
        else
            c(_)

    /**
     * converts a scala function into a [[MutationCommand]].
     *
     * @since MCDDPUI 0.1.0
     * @tparam T the input type of the command
     * @tparam U the output type of the command
     * @param f the function to be converted into a [[MutationCommand]].
     * 				May not be `null`.
     * @return a [[MutationCommand]] based off the function `f`
     * @throws NullPointerException if `f` is `null`
     * @example
     * {{{
     * import ch.waan.mcddpui.api._
     * val c0: MutationCommand[String, String] = (s: String) => s.toUpperCase
     * val c1: MutationCommand[String, String] = (_: String).toUpperCase
     * }}}
     */
    implicit def function2mutationCommand[T, U](f: T => U): MutationCommand[T, U] =
        if (f == null)
            throw new NullPointerException
        else
            new FunctionMutationCommand(f)

    /**
     * converts a [[MutationCommand]] into a scala function.
     *
     * @since MCDDPUI 0.1.0
     * @tparam T the input type of the command
     * @tparam U the output type of the command
     * @param c the command to be converted into a function.
     * 				May not be `null`.
     * @return a function based off the command `c`
     * @throws NullPointerException if `c` is `null`
     */
    implicit def mutationCommand2function[T, U](c: MutationCommand[T, U]): T => U =
        if (c == null)
            throw new NullPointerException
        else
            c(_)

}