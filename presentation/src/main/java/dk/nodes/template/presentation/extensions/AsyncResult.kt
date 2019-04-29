package dk.nodes.template.presentation.extensions

sealed class AsyncResult<out T>(val complete: Boolean, val shouldLoad: Boolean)

object Uninitialized : AsyncResult<Nothing>(complete = false, shouldLoad = true), Incomplete

class Loading<out T> : AsyncResult<T>(complete = false, shouldLoad = false), Incomplete {
    override fun equals(other: Any?) = other is Loading<*>

    override fun hashCode() = "Loading".hashCode()
}

data class Success<out T>(val data: T) : AsyncResult<T>(complete = true, shouldLoad = false)

data class Error<out T>(val error: Throwable) : AsyncResult<T>(complete = true, shouldLoad = true)

interface Incomplete
