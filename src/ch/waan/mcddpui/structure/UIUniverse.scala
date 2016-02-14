package ch.waan.mcddpui.structure

import scala.util.Try
import scala.util.Failure

case class UIUniverse[+T](data: T, viewStack: List[ViewData]) {
  def updateData[U](f: T => Try[U]) =
    if (f == null)
      Failure(new NullPointerException("cannot update with [null] function"))
    else
      f(data).map(UIUniverse(_, viewStack))
  def updateViewStack(f: List[ViewData] => Try[List[ViewData]]) =
    if (f == null)
      Failure(new NullPointerException("cannot update with [null] function"))
    else
      f(viewStack).map(UIUniverse(data, _))
}

trait ViewData
case class RootViewData() extends ViewData