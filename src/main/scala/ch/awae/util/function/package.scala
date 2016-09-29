package ch.awae.util

import scala.language.higherKinds
import scala.language.implicitConversions

package object function {

  type Functor[-F[_], +G[_]] = (F ~> G)
  type Id[T] = T
  type Wrapper[+F[_]] = (Id ~> F)

  implicit def functorProvider2functor[F[_], G[_]](f: FunctorProvider[F, G]) = f.functor

}