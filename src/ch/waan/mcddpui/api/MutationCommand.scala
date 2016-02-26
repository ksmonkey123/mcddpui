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
 * @see [[Command.get[T,U]* Command.get]]
 *
 * @author Andreas Waelchli <andreas.waelchli@me.com>
 * @version 1.1 (0.1.0), 2016-02-26
 * @since MCDDPUI 0.1.0
 *
 * @tparam T the input type
 * @tparam U the output type
 */
trait MutationCommand[T, U] {

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
        }
    }

}