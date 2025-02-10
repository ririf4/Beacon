@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package net.ririfa.beacon

/**
 * Represents a hook for an event.
 *
 * @param T The type of event.
 * @property handlerClass The class of the event handler.
 * @property handler The handler function for the event.
 * @property ignoresCondition Whether the condition should be ignored.
 * @property priority The priority of the event hook.
 * @property condition An optional condition that must be met for the handler to be executed.
 * @property timeout An optional timeout for the event hook.
 */
class EventHook<T : Event>(
    val handlerClass: IEventHandler,
    val handler: Handler<T>,
    val ignoresCondition: Boolean,
    val priority: Priority = Priority.NORMAL,
    val condition: (() -> Boolean)? = null,
    val timeout: Long? = null
)

/**
 * Represents a hook for a returnable event.
 *
 * @param T The type of event.
 * @param R The type of the result returned by the handler.
 * @property handlerClass The class of the event handler.
 * @property handler The handler function for the event.
 * @property ignoresCondition Whether the condition should be ignored.
 * @property priority The priority of the event hook.
 * @property condition An optional condition that must be met for the handler to be executed.
 * @property timeout An optional timeout for the event hook.
 */
class ReturnableEventHook<T : ReturnableEvent<R>, R>(
    val handlerClass: IEventHandler,
    val handler: ReturnableHandler<T, R>,
    val ignoresCondition: Boolean,
    val priority: Priority = Priority.NORMAL,
    val condition: (() -> Boolean)? = null,
    val timeout: Long? = null
)

/**
 * Registers an event handler for a specific event type.
 *
 * @param T The type of event.
 * @param condition An optional condition that must be met for the handler to be executed.
 * @param ignoresCondition Whether the condition should be ignored.
 * @param priority The priority of the event hook.
 * @param timeout An optional timeout for the event hook.
 * @param handler The handler function for the event.
 * @throws IllegalStateException If the class does not implement IEventHandler.
 */
inline fun <reified T : Event> IEventHandler.handler(
    noinline condition: () -> Boolean = { true },
    ignoresCondition: Boolean = false,
    priority: Priority = Priority.NORMAL,
    timeout: Long? = null,
    noinline handler: (T) -> Unit
) {
    EventBus.registerEventHook(
        T::class.java,
        EventHook(
            this,
            Handler(handler),
            ignoresCondition,
            priority,
            condition,
            timeout
        )
    )
}

/**
 * Registers a returnable event handler for a specific event type.
 *
 * @param T The type of event.
 * @param R The type of the result returned by the handler.
 * @param condition An optional condition that must be met for the handler to be executed.
 * @param ignoresCondition Whether the condition should be ignored.
 * @param priority The priority of the event hook.
 * @param timeout An optional timeout for the event hook.
 * @param handler The handler function for the event.
 * @throws IllegalStateException If the class does not implement IEventHandler.
 */
inline fun <reified T : ReturnableEvent<R>, R> IEventHandler.returnableHandler(
    noinline condition: () -> Boolean = { true },
    ignoresCondition: Boolean = false,
    priority: Priority = Priority.NORMAL,
    timeout: Long? = null,
    noinline returnableHandler: (T) -> R
) {
    EventBus.registerReturnableEventHook(
        T::class.java,
        ReturnableEventHook(
            this,
            ReturnableHandler(returnableHandler),
            ignoresCondition,
            priority,
            condition,
            timeout
        )
    )
}

enum class EventProcessingType {
    /**
     * Synchronous event processing.
     */
    HANDLER_ASYNC,

    /**
     * Asynchronous event processing.
     */
    ASYNC,

    /**
     * Synchronous event processing (alias for Sync).
     */
    FULL_SYNC;

    companion object {
        fun fromString(type: String): EventProcessingType {
            return when (type.uppercase()) {
                "ASYNC" -> ASYNC
                "HANDLERASYNC" -> HANDLER_ASYNC
                "FULLSYNC" -> FULL_SYNC
                else -> throw IllegalArgumentException("Unknown EventProcessingType: $type")
            }
        }
    }
}

