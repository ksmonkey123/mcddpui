package ch.waan.mcddpui.structure

import scala.util.Try
import scala.util.Failure
import scala.util.Success

trait Record[T] {
  def update(f: T => Try[T]): Option[Throwable]
  def passTo(f: T => Option[Throwable]): Option[Throwable]
  def undo: Option[RecordHistoryManipulationException]
  def redo: Option[RecordHistoryManipulationException]
}

class RecordHistoryManipulationException(msg: String) extends Exception(msg)