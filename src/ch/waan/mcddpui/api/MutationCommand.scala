package ch.waan.mcddpui.api

/**
 * A mutation command represents a function transforming a given (immutable)
 * data structure into another (immutable) data structure. Any mutation may
 * result in any arbitrary exception or error. Therefore error handling is
 * necessary at the command execution site.
 *
 * @note Immutability of the data structure cannot be enforced. Therefore
 * ensuring immutability (or at least apparent immutability) lies in the
 * responsibility of the user.
 *
 * @note The implicit conversion methods
 * 			[[function2mutationCommand]] and
 * 			[[mutationCommand2function]] can be used to convert
 * 			freely between types `T => Unit` and `ReadCommand[T]`.
 *
 * @author Andreas Waelchli <andreas.waelchli@me.com>
 * @version 1.3 (0.2.0), 2016-03-01
 * @since MCDDPUI 0.1.0
 *
 * @tparam T the input type
 * @tparam U the output type
 */
trait MutationCommand[T, U] extends CommandLike {

    /**
     * The transformation function for this command.
     *
     * Any value returned by this function must be assumed to be valid
     * at the command execution site. This must also include `null`.
     * Any exceptional case must be indicated by throwing a Throwable.
     *
     * @note Immutability of the data structure cannot be enforced.
     * Therefore ensuring immutability (or at least apparent immutability)
     * lies in the responsibility of the user.
     *
     * @param t the input
     * @return a new transformed data structure. May be `null`.
     * @throws Throwable if any exceptional case occurs
     */
    @throws(classOf[Throwable])
    def apply(t: T): U;

    /**
     * The name of this command.
     *
     * This is targeted at UI applications where multi-path redo is possible.
     * In such a situation the names of the commands can be useful to distinguish
     * different redo paths.
     *
     * @return the name of this command.
     */
    def name: String

    /**
     * Chains this command with second one.
     *
     * @tparam V the output type of the second command
     * @param g the command to chain with this one. May not be `null`.
     * @return a [[MutationCommand]] with the functionality of this one
     *         chained with `g`.
     * @throws NullPointerException if `g` is `null`
     */
    def andThen[V](g: MutationCommand[_ >: U, V]): MutationCommand[T, V] = {
        if (g == null) throw new NullPointerException
        val f = this
        new MutationCommand[T, V] {
            override def apply(t: T): V = g(f(t))
            override def name = ???
        }
    }

}

/**
 * A [[MutationCommand]] backed by a scala function
 *
 * @author Andreas Waelchli <andreas.waelchli@me.com>
 * @version 1.1 (0.2.0), 2016-03-02
 * @since MCDDPUI 0.2.0
 *
 * @tparam T the input type
 * @tparam U the output type
 *
 * @constructor creates a new command instance
 * @param ƒ the function the command is based off. may not be `null`.
 * @param name the name of the command. may be `null`.
 * @throws NullPointerException if `ƒ` is `null`
 */
case class FunctionMutationCommand[T, U](ƒ: T => U, name: String = null) extends MutationCommand[T, U] {
    if (ƒ == null) throw new NullPointerException
    @throws(classOf[Throwable])
    override def apply(t: T): U = ƒ(t)
}

/**
 * A [[MutationCommand]] backed by a java lambda
 *
 * @author Andreas Waelchli <andreas.waelchli@me.com>
 * @version 1.1 (0.2.0), 2016-03-02
 * @since MCDDPUI 0.2.0
 *
 * @tparam T the input type
 * @tparam U the output type
 *
 * @constructor creates a new command instance
 * @param λ the lambda the command is based off. may not be `null`.
 * @param _name the name of the command. may be `null`.
 * @throws NullPointerException if `λ` is `null`.
 */
class LambdaMutationCommand[T, U](λ: MutationFunction[T, U], _name: String) extends MutationCommand[T, U] {
    /**
     * Convenience constructor
     *
     * Defaults the `_name` parameter to `null`.
     *
     * @param λ the lambda the command is based off. may not be `null`.
     * @throws NullPointerException if `λ` is `null`.
     */
    def this(λ: MutationFunction[T, U]) = this(λ, null)
    if (λ == null) throw new NullPointerException
    @throws(classOf[Throwable])
    override def apply(t: T): U = λ(t)
    override def name = _name
}

/**
 * A [[MutationCommand]] defined as the concatenation of two commands.
 *
 * @author Andreas Waelchli <andreas.waelchli@me.com>
 * @version 1.1 (0.2.0), 2016-03-02
 * @since MCDDPUI 0.2.0
 *
 * @tparam T the input type
 * @tparam U the intermediate type
 * @tparam V the output type
 *
 * @constructor creates a new command instance
 * @param f the first command. may not be `null`.
 * @param g the second command. may not be `null`.
 * @param _name the name of the command. may be `null`.
 * @throws NullPointerException if `f` or `g` is `null`.
 */
class ComposedMutationCommand[T, U, V](f: MutationCommand[T, U], g: MutationCommand[_ >: U, V], _name: String) extends MutationCommand[T, V] {

    /**
     * creates a new command instance with the name constructed from the names of the arguments.
     *
     * The name of the composed command is defined as:
     *   1. if both names are `null`: `null`.
     *   1. if one name is not `null`: that name.
     *   1. if both names are not `null`: both names in order separated by "` & `".
     *
     * @param f the first command. may not be `null`.
     * @param g the second command. may not be `null`.
     * @throws NullPointerException if `f` or `g` is `null`.
     */
    def this(f: MutationCommand[T, U], g: MutationCommand[_ >: U, V]) = this(f, g, (f.name, g.name) match {
        case (null, null) => null
        case (x, null)    => x
        case (null, y)    => y
        case (x, y)       => x + " & " + y
    })

    if ((f == null) || (g == null)) throw new NullPointerException

    @throws(classOf[Throwable])
    override def apply(t: T): V = g(f(t))
    override def name = _name

}