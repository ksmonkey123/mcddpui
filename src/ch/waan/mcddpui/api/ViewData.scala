package ch.waan.mcddpui.api

/**
 * common base trait for all view data objects.
 *
 * A view data object represents a particular UI state
 * in the internal data structure of a UI application.
 * implementations of this trait should always be
 * immutable to allow for proper thread-safe object
 * sharing in multi-threaded applications.
 * 
 * @author Andreas Waelchli <andreas.waelchli@me.com>
 * @version 1.1 (0.1.0), 2016-02-29
 * @since MCDDPUI 0.1.0
 */
trait ViewData