package net.ririfa.beacon;

import java.util.List;

/**
 * Interface representing an event handler.
 */
public interface IEventHandler {

    /**
     * Please write a handler initialization code.
     *
     * <p>For example:
     * <pre>{@code
     * @Override
     * public void initHandlers() {
     *   handler(this, MyEvent.class, event -> {
     *     System.out.println("This is a handler for MyEvent: " + event.getMessage());
     *     System.out.println("Thread: " + Thread.currentThread().getName());
     *     return Unit.INSTANCE;
     *   });
     *   handler(this, MyReturnableEvent.class, event -> {
     *     System.out.println("This is a handler for MyReturnableEvent: " + event.getMessage());
     *     System.out.println("Thread: " + Thread.currentThread().getName());
     *     return "Hello from returnable handler!";
     *   });
     * }
     * }</pre>
     */
    void initHandlers();

    /**
     * Handles events for this handler.
     *
     * @return true if the events are handled successfully, false otherwise
     */
    default boolean handleEvents() {
        IEventHandler parent = parent();
        return parent == null || parent.handleEvents();
    }

    /**
     * Returns the parent event handler.
     *
     * @return the parent event handler, or null if there is no parent
     */
    default IEventHandler parent() {
        return null;
    }

    /**
     * Returns the list of child event handlers.
     *
     * @return a list of child event handlers
     */
    default List<IEventHandler> children() {
        return List.of();
    }

    /**
     * Unregisters this event handler and all its children.
     */
    default void unregister() {
        for (IEventHandler child : children()) {
            child.unregister();
        }
    }
}