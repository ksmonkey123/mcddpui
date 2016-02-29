package ch.waan.mcddpui.api

import ch.waan.mcddpui.exceptions.RecordHistoryManipulationException

/**
 * a record encapsulates an immutable data structure in a modifiable
 * container with a modification history and undo/redo functionality
 *
 * @note Immutability of the data structure cannot be enforced.
 * Therefore ensuring immutability (or at least apparent immutability)
 * lies in the responsibility of the user.
 *
 * @tparam T the type of the encapsulated data structure
 *
 * @author Andreas Waelchli <andreas.waelchli@me.com>
 * @version 1.1 (0.1.0), 2016-02-29
 * @since MCDDPUI 0.1.0
 */
trait Record[T] {

    /**
     * passes the internal data structure to the [[ReadCommand]]
     *
     * @param c the command to execute
     * @throws Throwable if any exceptional condition occurs during
     * 						command execution
     */
    @throws(classOf[Throwable])
    def view(c: ReadCommand[_ >: T]): Unit

    /**
     * updates the internal data structure through the [[MutationCommand]]
     *
     * @param c the command to update the data structure with
     * @throws Throwable if any exceptional condition occurs during
     * 						command execution
     */
    @throws(classOf[Throwable])
    def update(c: MutationCommand[_ >: T, _ <: T]): Unit

    /**
     * undoes the last modification
     *
     * @throws RecordHistoryManipulationException if the undo failed
     */
    @throws(classOf[RecordHistoryManipulationException])
    def undo: Unit

    /**
     * redoes the last modification undone
     *
     * @throws RecordHistoryManipulationException if the redo failed
     */
    @throws(classOf[RecordHistoryManipulationException])
    def redo: Unit

}