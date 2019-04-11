package dk.nodes.template.domain.extensions

import dk.nodes.template.domain.interactors.IResult

inline fun <T> T.guard(block: T.() -> Unit): T {
    if (this == null) block(); return this
}

inline fun <T : Any> safeCall(
    call: () -> T
): IResult<T> {
    return try {
        val value = call()
        IResult.Success(value)
    } catch (e: Exception) {
        // An throwable was thrown when calling, so we're converting this to an IOException
        IResult.Error(e)
    }
}