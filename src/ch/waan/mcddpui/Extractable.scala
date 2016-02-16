package ch.waan.mcddpui

trait Extractable[T] {
  def extract: T
}

trait SelfExtraction[T <: SelfExtraction[T]] extends Extractable[T] {
  self: T =>
  def extract: T = self
}
