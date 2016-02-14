package ch.waan.mcddpui.structure

import scala.util.Try
import scala.util.Success
import scala.util.Success

case class DataUpdateCommand[T](f: T => Try[T])
    extends MutationCommand[UIUniverse[T], UIUniverse[T]] {
  if (f == null) throw new NullPointerException("cannot create [null] command")
  def apply(u: UIUniverse[T]) = u.updateData(f)
}

object DataUpdateCommand {
  def apply[T](f: T => T)(implicit ev: T <:< Any): DataUpdateCommand[T] =
    DataUpdateCommand[T]((x: T) => Try(f(x)))
}