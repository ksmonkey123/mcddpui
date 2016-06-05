package ch.waan.util.function

import scala.language.higherKinds

trait ~>[-F[_], +G[_]] {
    def apply[A](a: F[A]): G[A]

    def specific[A]: F[A] => G[A] = this.apply

    def andThen[H[_]](λ: G ~> H): F ~> H = {
        val ƒ = this
        new (F ~> H) {
            def apply[A](a: F[A]) = λ(ƒ(a))
        }
    }

    def andThen[X, Y](λ: G[X] => Y): F[X] => Y = this.specific.andThen(λ)
}

trait FunctorProvider[-F[_], +G[_]] {
    def functor: (F ~> G)
}