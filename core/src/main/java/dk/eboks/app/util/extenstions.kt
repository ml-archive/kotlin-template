package dk.eboks.app.util

import android.app.Activity
import android.view.View
import android.view.inputmethod.InputMethodManager
import dk.eboks.app.domain.exceptions.ServerErrorException
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.channel.Channel
import dk.eboks.app.domain.models.local.ViewError
import dk.nodes.arch.domain.interactor.BaseInteractor
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

inline fun <T> T.guard(block: T.() -> Unit): T {
    if (this == null) block(); return this
}

fun View.hideKeyboad() {
    val inputMethodManager =
        context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(this.windowToken, 0)
}

fun BaseInteractor.exceptionToViewError(
    t: Throwable,
    shouldClose: Boolean = false,
    shouldDisplay: Boolean = true
): ViewError {
    t.cause?.let {
        return throwableToViewError(it, shouldClose, shouldDisplay)
    }.guard {
        return throwableToViewError(t, shouldClose, shouldDisplay)
    }
    return ViewError(
        shouldDisplay = shouldDisplay,
        shouldCloseView = shouldClose
    )
}

internal fun throwableToViewError(
    t: Throwable,
    shouldClose: Boolean = false,
    shouldDisplay: Boolean = true
): ViewError {
    when (t) {
        is ConnectException -> return ViewError(
            title = Translation.error.noInternetTitle,
            message = Translation.error.noInternetMessage,
            shouldDisplay = shouldDisplay,
            shouldCloseView = shouldClose
        )
        is UnknownHostException -> return ViewError(
            title = Translation.error.noInternetTitle,
            message = Translation.error.noInternetMessage,
            shouldDisplay = shouldDisplay,
            shouldCloseView = shouldClose
        )
        is IOException -> return ViewError(
            title = Translation.error.genericStorageTitle,
            message = Translation.error.genericStorageMessage,
            shouldDisplay = shouldDisplay,
            shouldCloseView = shouldClose
        )
        is SocketTimeoutException -> return ViewError(
            title = Translation.error.noInternetTitle,
            message = Translation.error.noInternetMessage,
            shouldDisplay = shouldDisplay,
            shouldCloseView = shouldClose
        )
        is ServerErrorException -> {
            return ViewError(
                title = t.error.description?.title,
                message = t.error.description?.text,
                shouldDisplay = shouldDisplay,
                shouldCloseView = shouldClose
            )
        }
        else -> return ViewError(
            shouldDisplay = shouldDisplay,
            shouldCloseView = shouldClose
        )
    }
}

fun Channel.areAllRequirementsVerified(): Boolean {
    this.requirements?.let { reqs ->
        for (req in reqs) {
            req.verified?.let {
                if (!it)
                    return false
            }
        }
        return true
    }.guard { return true }
    return true
}

val Channel.type: ChannelType
    get() {
        return when (id) {
            in 1..3 -> ChannelType.Storebox
            in 11..13 -> ChannelType.Ekey
            else -> ChannelType.Channel
        }
    }

enum class ChannelType {
    Storebox, Ekey, Channel
}