package dk.eboks.app.util

import android.view.View
import android.view.ViewGroup
import dk.eboks.app.domain.exceptions.ServerErrorException
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.local.ViewError
import dk.nodes.arch.domain.interactor.BaseInteractor
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

fun ViewGroup.asSequence(): Sequence<View> = object : Sequence<View> {
    override fun iterator(): Iterator<View> = object : Iterator<View> {
        private var nextValue: View? = null
        private var done = false
        private var position: Int = 0

        override fun hasNext(): Boolean {
            if (nextValue == null && !done) {
                nextValue = getChildAt(position)
                position++
                if (nextValue == null) done = true
            }
            return nextValue != null
        }

        override fun next(): View {
            if (!hasNext()) {
                throw NoSuchElementException()
            }
            val answer = nextValue
            nextValue = null
            return answer!!
        }
    }
}

val ViewGroup.views: List<View>
    get() = asSequence().toList()

inline fun <T> T.guard(block: T.() -> Unit): T {
    if (this == null) block(); return this
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
    return dk.eboks.app.domain.models.local.ViewError(shouldDisplay = shouldDisplay, shouldCloseView = shouldClose)
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
