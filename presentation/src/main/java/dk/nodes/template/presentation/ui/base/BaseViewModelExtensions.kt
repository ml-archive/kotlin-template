package dk.nodes.template.presentation.ui.base

import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import java.io.Closeable
import kotlin.coroutines.CoroutineContext

private const val jobKey = "dk.nodes.template.presentation.base.JOB_KEY"
private const val disposablesKey = "dk.nodes.template.presentation.base.DISPOSABLES_KEY"

val BaseViewModel.scope: CoroutineScope
    get() {
        return getTag(jobKey) ?: setTag(
            jobKey,
            CloseableCoroutineScope(Job() + Dispatchers.Main)
        )
    }

val BaseViewModel.disposables: CompositeDisposable
    get() {
        return getTag<CloseableCompositeDisposable>(disposablesKey)?.disposables ?: setTag(
            disposablesKey, CloseableCompositeDisposable()
        ).disposables
    }

private class CloseableCoroutineScope(context: CoroutineContext) : Closeable, CoroutineScope {
    override val coroutineContext: CoroutineContext = context
    override fun close() {
        coroutineContext.cancel()
    }
}

private class CloseableCompositeDisposable : Closeable {
    val disposables = CompositeDisposable()
    override fun close() {
        disposables.dispose()
    }
}