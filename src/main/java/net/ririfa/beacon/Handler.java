package net.ririfa.beacon;

/**
 * Functional interface representing a handler for events of type T.
 *
 * @param <T> the type of the event to be handled
 */
@FunctionalInterface
public interface Handler<T> {
    /**
     * Handles the given event.
     *
     * @param event the event to be handled
     */
    void handle(T event);
}