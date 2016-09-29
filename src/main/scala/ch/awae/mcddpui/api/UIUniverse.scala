package ch.awae.mcddpui.api

import java.util.function.Function

/**
 * data structure root node for UI applications.
 *
 * Holds the operational data as well as an abstract
 * representation encoding the state of the UI. This
 * allows for full historizing not only operational
 * data but for UI state changes as well.
 *
 * @author Andreas Waelchli <andreas.waelchli@me.com>
 * @version 1.1 (0.1.0), 2016-02-29
 * @since MCDDPUI 0.1.0
 *
 * @tparam T the data type of the operational data
 * @param data the operational data
 * @param ui the UI state representation
 */
@SerialVersionUID(0L)
case class UIUniverse[T](data: T, ui: UIData) {

    def updateData(ƒ: T => T) = UIUniverse(ƒ(data), ui)
    def updateData(λ: Function[T, T]) = UIUniverse(λ(data), ui)

    def updateUI(ƒ: UIData => UIData) = UIUniverse(data, ƒ(ui))
    def updateUI(λ: Function[UIData, UIData]) = UIUniverse(data, λ(ui))

}