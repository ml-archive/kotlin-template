package dk.nodes.arch.domain.interactor

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

interface Interactor<in P> {
    val dispatcher: CoroutineDispatcher
    suspend operator fun invoke(executeParams: P)
}

interface ResultInteractor<in P, out R : Any> {
    val dispatcher: CoroutineDispatcher
    suspend operator fun invoke(executeParams: P, onResult: (Result<R>) -> Unit)
}

fun <P> CoroutineScope.launchInteractor(interactor: Interactor<P>, param: P): Job {
    return launch(context = interactor.dispatcher, block = { interactor(param) })
}

fun CoroutineScope.launchInteractor(interactor: Interactor<Unit>) =
    launchInteractor(interactor, Unit)

fun <P, R : Any> CoroutineScope.launchInteractor(
    interactor: ResultInteractor<P, R>,
    param: P,
    onResult: (Result<R>) -> Unit
): Job {
    return launch(context = interactor.dispatcher, block = { interactor(param, onResult) })
}

fun <R : Any> CoroutineScope.launchInteractor(
    interactor: ResultInteractor<Unit, R>,
    onResult: (Result<R>) -> Unit
) = launchInteractor(interactor, Unit, onResult)