package ch.waan.mcddpui.ui

import ch.waan.mcddpui.structure.ViewData
import scala.collection.immutable.HashMap
import java.util.UUID
import ch.waan.mcddpui.structure.UIUniverse
import scala.util.Failure
import scala.util.Success
import ch.waan.mcddpui.structure.RootViewData

abstract class ViewManager[T] {

  protected def showView(id: String): Option[Throwable];
  protected def addView(view: View[T], id: String): Option[Throwable];

  final def registerView(view: View[T]) =
    if (view == null) Some(new NullPointerException("cannot register [null] view"))
    else {
      val id = UUID.randomUUID.toString
      val res = addView(view, id)
      if (res.isEmpty)
        views += ((id, view))
      res
    }

  final def updateView(u: UIUniverse[T]) =
    if (u == null) Some(new NullPointerException("cannot update from [null] universe"))
    else {
      views
        .map(kv => (kv._1, kv._2.updater))
        .find(kf => kf._2.isDefinedAt(u.data, u.viewStack.head))
        .map(kf => (kf._1, kf._2.apply(u.data, u.viewStack.head)))
        .flatMap(kf => kf._2.orElse(showView(kf._1)))
    }

  @volatile private[this] var views = HashMap.empty[String, View[T]]

}