package dk.nodes.template.presentation.util

import androidx.lifecycle.Observer

/**
 * Used as a wrapper for data that is exposed via a LiveData that represents an event.
 *
 * [Read more about this.](https://medium.com/google-developers/livedata-with-snackbar-navigation-and-other-events-the-singleliveevent-case-ac2622673150)
 */
open class SingleEvent<out T>(private val content: T) {

    var consumed = false
        private set // Allow external read but not write

    /**
     * Consumes the content if it's not been consumed yet.
     * @return The unconsumed content or `null` if it was consumed already.
     */
    fun consume(): T? {
        return if (consumed) {
            null
        } else {
            consumed = true
            content
        }
    }

    /**
     * @return The content whether it's been handled or not.
     */
    fun peek(): T = content

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SingleEvent<*>

        if (content != other.content) return false
        if (consumed != other.consumed) return false

        return true
    }

    override fun hashCode(): Int {
        var result = content?.hashCode() ?: 0
        result = 31 * result + consumed.hashCode()
        return result
    }
}

fun <T> SingleEvent<T>?.consume(function: (T) -> Unit) {
    this?.consume()?.let(function)
}

/**
 * An [Observer] for [SingleEvent]s, simplifying the pattern of checking if the [SingleEvent]'s content has
 * already been consumed.
 *
 * [onEventUnconsumedContent] is *only* called if the [SingleEvent]'s contents has not been consumed.
 */

class EventObserver<T>(private val onEventUnconsumedContent: (T) -> Unit) : Observer<SingleEvent<T>> {
    override fun onChanged(event: SingleEvent<T>?) {
        event?.consume()?.run(onEventUnconsumedContent)
    }
}