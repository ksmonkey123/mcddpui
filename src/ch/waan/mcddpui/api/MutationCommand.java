package ch.waan.mcddpui.api;

import java.util.Objects;

@FunctionalInterface
public interface MutationCommand<T, U> {

	U apply(T t) throws Throwable;

	default <V> MutationCommand<T, V> andThen(final MutationCommand<? super U, V> g) {
		Objects.requireNonNull(g);
		final MutationCommand<T, U> f = this;

		return new MutationCommand<T, V>() {
			@Override
			public V apply(T t) throws Throwable {
				return g.apply(f.apply(t));
			}
		};
	}

}
