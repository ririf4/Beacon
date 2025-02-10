@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package net.ririfa.beacon

/**
 * Represents a generic event.
 */
abstract class Event {
    /**
     * The metadata of the event.
     * This can be used to store additional information about the event.
     */
    @Deprecated("I think Event class constructor is more useful than this system.")
    val metadata: MutableMap<String, Any?> = mutableMapOf()

    /**
     * Sets a metadata key-value pair.
     * @param key The key of the metadata.
     * @param value The value of the metadata.
     */
    @Deprecated("I think Event class constructor is more useful than this system.")
    fun setMeta(key: String, value: Any) = metadata.put(key, value)

    /**
     * Gets a metadata value by key.
     * @param key The key of the metadata.
     * @return The value of the metadata.
     */
    @Deprecated("I think Event class constructor is more useful than this system.")
    fun getMeta(key: String): Any? = metadata[key]

    /**
     * Gets a metadata value by key or a default value if the value is null.
     * @param key The key of the metadata.
     * @param default The default value to return if the value is null.
     * @return The value of the metadata or the default value.
     */
    @Deprecated("I think Event class constructor is more useful than this system.")
    fun <T> getMetaOrDefault(key: String, default: T): T = metadata[key] as? T ?: default
}

/**
 * Represents an event that can be canceled.
 */
abstract class CancelableEvent : Event() {
    /**
     * Indicates whether the event is canceled.
     */
    var isCanceled: Boolean = false
        private set

    /**
     * Cancels the event.
     */
    fun cancel() {
        isCanceled = true
    }

    /**
     * Uncancels the event.
     */
    fun reset() {
        isCanceled = false
    }
}

/**
 * Represents an event that can return a result.
 * @param T The type of the result.
 */
abstract class ReturnableEvent<T> : Event() {
    /**
     * The result of the event.
     */
    var result: T? = null
        private set

    /**
     * Sets the result of the event.
     * @param result The result to set.
     */
    fun setResult(result: T) {
        this.result = result
    }

    /**
     * Gets the result of the event or a default value if the result is null.
     * @param default The default value to return if the result is null.
     * @return The result of the event or the default value.
     */
    fun getResultOrDefault(default: T): T = result ?: default
}

/**
 * Represents the priority of an event.
 * @param level The integer value of the priority.
 */
enum class Priority(val level: Int) {
    LOWEST(0),
    LOW(1),
    MID_LOW(2),
    NORMAL(3),
    MID_HIGH(4),
    HIGH(5),
    HIGHEST(6)
}