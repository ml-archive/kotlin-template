package dk.nodes.arch.extensions

import java.io.IOException

/**
 * Wrap a [call] in try/catch. In case an exception is thrown, a [Result.error] is
 * created based on the [errorMessage].
 */
fun <T : Any> safeCall(call: () -> T, errorMessage: String, onResult: (Result<T>) -> Unit) {
    try {
        val value = call()
        onResult.invoke(Result.success(value))
    } catch (e: Exception) {
        // An exception was thrown when calling, so we're converting this to an IOException
        onResult.invoke(Result.failure(IOException(errorMessage, e)))
    }
}

suspend fun <T : Any> safeSuspendCall(
    call: suspend () -> T,
    errorMessage: String,
    onResult: (Result<T>) -> Unit
) {
    try {
        val value = call()
        onResult.invoke(Result.success(value))
    } catch (e: Exception) {
        // An exception was thrown when calling, so we're converting this to an IOException
        onResult.invoke(Result.failure(IOException(errorMessage, e)))
    }
}