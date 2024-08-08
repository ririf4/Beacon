package net.rk4z.beacon;

import java.util.List;

/**
 * Interface representing an event handler.
 */
public interface IEventHandler {

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