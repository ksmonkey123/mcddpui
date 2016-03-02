package ch.waan.mcddpui.api

import scala.runtime.BoxedUnit

/**
 * collection of implicit conversion methods and implicit classes
 * extending the basic functionality of the java API to better suit
 * scala implementations.
 *
 * {{{
 * // import all implicits
 * import ch.waan.mcddpui.api.Implicits._
 * }}}
 *
 * @author Andreas Waelchli <andreas.waelchli@me.com>
 * @version 1.1 (0.1.0), 2016-02-29
 * @since MCDDPUI 0.1.0
 */
object Implicits {


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
     * import ch.waan.mcddpui.api.Implicits._
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
     * import ch.waan.mcddpui.api.Implicits._
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