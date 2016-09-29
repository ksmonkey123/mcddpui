package ch.awae.util.function

import scala.language.higherKinds

trait ~>[-F[_], +G[_]] extends FunctorProvider[F, G] {

    def apply[T](a: F[T]): G[T]

    def specific[T] = apply[T](_)

    def andThen[H[_]](g: ~>[G, H]): ~>[F, H] = {
        val f = this
        new (F ~> H) {
            def apply[T](a: F[T]): H[T] = g(f(a))
        }
    }

    def andThen[T, U](λ: G[T] => U): F[T] => U = specific[T] andThen λ

    def functor = this

}

trait FunctorProvider[-F[_], +G[_]] {

    def functor: ~>[F, G]

}