package dk.eboks.app.network.util

import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.local.ViewError
import retrofit2.Response

fun Response<*>.errorBodyToViewError(
    shouldClose: Boolean = false
): ViewError {
    val responseString = errorBody()?.string()

    // Todo add more cases like 401,402, ect ect

    return when (code()) {
        else -> {
            ViewError(
                Translation.error.genericTitle,
                Translation.error.genericMessage,
                true,
                shouldClose
            )
        }
    }
}