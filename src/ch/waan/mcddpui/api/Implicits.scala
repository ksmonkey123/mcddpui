package ch.waan.mcddpui.api

import scala.runtime.BoxedUnit

object Implicits {

    implicit def function2readCommand[T](f: T => Unit): ReadCommand[T] = new ReadCommand[T] {
        override def apply(t: T) = f(t)
    }

    implicit def readCommand2function[T](c: ReadCommand[T]) = c(_)

    implicit def function2mutationCommand[T, U](f: T => U): MutationCommand[T, U] = new MutationCommand[T, U] {
        override def apply(t: T) = f(t)
    }

    implicit def mutationCommand2function[T, U](c: MutationCommand[T, U]) = c(_)

    implicit def voidFunction2BoxedUnitFunction[T](f: T => Unit): T => BoxedUnit = f.andThen(_ => BoxedUnit.UNIT)

}