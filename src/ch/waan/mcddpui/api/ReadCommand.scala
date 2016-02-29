package ch.waan.mcddpui.api

/**
 * A read command represents a function inspecting a (preferably immutable)
 * data structure without effect in said data structure. A read command may
 * also have side-effects. A read command may result in any arbitrary
 * exception or error. Therefore error handling is necessary at the command
 * execution site. If a read command has side effects the command
 * implementation must ensure a valid (preferably unaffected) state of any
 * (potentially) affected resource.
 *
 * @note Immutability of the data structure cannot be enforced. Therefore
 * 			ensuring immutability (or at least ensuring the lack of any
 * 			mutating operations on the data structure) lies in the
 * 			responsibility of the user.
 *
 * @note The implicit conversion methods [[Implicits.function2readCommand]]
 * 			and [[Implicits.readCommand2function]] can be used to convert
 * 			freely between types `T => Unit` and `ReadCommand[T]`.
 *
 * @author Andreas Waelchli <andreas.waelchli@me.com>
 * @version 1.2, 2016-02-29
 * @since MCDDPUI 0.1.0
 * @see [[Implicits]]
 *
 * @tparam T the input type for the command
 */
@FunctionalInterface
trait ReadCommand[T] extends Command {

    /**
     * The read function for this command.
     *
     * @note Immutability of the data structure cannot be enforced.
     * Therefore ensuring immutability (or at least apparent immutability)
     * lies in the responsibility of the user.
     *
     * @param t the input
     * @throws Throwable if any exceptional case occurs
     */
    @throws(classOf[Throwable])
    def apply(t: T): Unit

}