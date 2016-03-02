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
 * @version 1.2 (0.2.0), 2016-03-02
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
    @throws[Throwable]
    def view(c: ReadCommand[_ >: T]): Unit

    /**
     * updates the internal data structure through the [[MutationCommand]]
     *
     * @param c the command to update the data structure with
     * @throws Throwable if any exceptional condition occurs during
     * 						command execution
     */
    @throws[Throwable]
    def update(c: MutationCommand[_ >: T, _ <: T]): Unit

    /**
     * undoes the last modification
     *
     * @throws RecordHistoryManipulationException if the undo failed
     */
    @throws[RecordHistoryManipulationException]
    def undo: Unit

    /**
     * redoes the along a given path
     *
     * @param index the index of the redo path
     * @throws RecordHistoryManipulationException if the redo failed
     * @throws IndexOutOfBoundsException if the redo path index is out of bounds
     */

    @throws[RecordHistoryManipulationException]
    @throws[IndexOutOfBoundsException]
    def redo(index: Int): Unit

    /**
     * lists all possible redo paths in order
     *
     * @return a list containing the names for all possible redo paths
     */
    def listRedoPaths: List[String]

}