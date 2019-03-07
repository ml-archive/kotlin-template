package dk.nodes.template.domain.interactors

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

suspend fun <O> runInteractor(
    context: CoroutineContext = Dispatchers.IO,
    interactor: BaseAsyncInteractor<O>
) = withContext(context) { interactor() }

fun <P> CoroutineScope.launchInteractor(
    context: CoroutineContext = Dispatchers.IO,
    interactor: BaseAsyncInteractor<P>
): Job {
    return launch(context = context, block = { interactor() })
}