package ch.waan.mcddpui.api;

public interface Record<T> {

	void view(ReadCommand<? super T> command) throws Throwable;

	void update(MutationCommand<? super T, ? extends T> command) throws Throwable;

	void undo() throws RecordHistoryManipulationException;

	void redo() throws RecordHistoryManipulationException;

}