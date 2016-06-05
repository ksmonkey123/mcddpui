package ch.waan.util

import scala.language.higherKinds

package object function {
    type Id[A] = A

    implicit def functorProvider2Functor[F[_], G[_]](provider: FunctorProvider[F, G]) =
        provider.functor

    implicit def functor2FunctorProvider[F[_], G[_]](ƒ: F ~> G) = new FunctorProvider[F, G] {
        def functor = ƒ
    }
}