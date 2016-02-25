package ch.waan.mcddpui.api;

@FunctionalInterface
public interface ReadCommand<T> {

	void apply(T t) throws Throwable;

}