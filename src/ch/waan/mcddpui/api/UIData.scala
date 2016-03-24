package ch.waan.mcddpui.api

import scala.collection.immutable.HashMap

/**
 * root object for the structural representation of the
 * UI state in a UI application. Contains a view stack
 * and an immutable hash map for representing global UI
 * state.
 *
 * @author Andreas Waelchli <andreas.waelchli@me.com>
 * @version 1.1 (0.1.0), 2016-02-29
 * @since MCDDPUI 0.1.0
 *
 * @param viewStack the stack containing the view data
 * 				representations in the order they are
 * 				layered. (opaque layers!).
 * @param props a hash map containing global UI properties
 */
@SerialVersionUID(0L)
case class UIData(
    viewStack: List[ViewData],
    props: HashMap[String, String] = HashMap.empty)