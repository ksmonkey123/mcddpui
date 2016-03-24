package ch.waan.mcddpui.api

/**
 * core view life-cycle management base trait.
 *
 * @author Andreas Waelchli <andreas.waelchli@me.com>
 * @version 1.1 (0.3.0), 2016-03-24
 * @since MCDDPUI 0.3.0
 *
 * @tparam T the type of the data structure
 *
 * @see [[View]] for full life-cycle documentation
 */
trait ViewManagerLike[T] {

    /**
     * register a view.
     *
     * views may not be duplicates of already registered ones.
     *
     * @param view the view to register. may not be `null`, may not be bound, must
     * 			be packed and may not be a duplicate of an already registered view
     * @throws NullPointerException if `view` is `null`
     * @throws IllegalArgumentException if `view` is bound, unpacked or a duplicate
     */
    @throws[NullPointerException]
    @throws[IllegalArgumentException]
    def register(view: View[T]): Unit

    /**
     * update the views from the data structure
     *
     * @see [[View]] for full documentation
     *
     * @param u the data structure to update from. may not be `null`
     * @return a list with all assigned views in the order of the view stack
     * @throws NullPointerException if `u` is `null`
     * @throws IllegalStateException if no view was found to be used for the top-level view stack entry
     */
    @throws[NullPointerException]
    @throws[IllegalStateException]
    def update(u: UIUniverse[T]): List[View[T]]

}