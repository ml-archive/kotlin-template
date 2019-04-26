package dk.nodes.template.domain.extensions

import dk.nodes.template.domain.interactors.BaseAsyncInteractor
import dk.nodes.template.domain.interactors.InteractorResult

inline fun <T> T.guard(block: T.() -> Unit): T {
    if (this == null) block(); return this
}

suspend fun <I, O : Any> BaseAsyncInteractor<I, O>.asResult(input: I): InteractorResult<O> {
    return ResultInteractor(this)(input)
}

suspend fun <O : Any> BaseAsyncInteractor<Unit, O>.asResult(): InteractorResult<O> {
    return ResultInteractor(this)(Unit)
}

suspend operator fun <O> BaseAsyncInteractor<Unit, O>.invoke(): O {
    return invoke(Unit)
}

private class ResultInteractor<I, O : Any>(private val interactor: BaseAsyncInteractor<I, O>) :
    BaseAsyncInteractor<I, InteractorResult<O>> {
    override suspend fun invoke(input: I): InteractorResult<O> {
        return try {
            InteractorResult.Success(interactor(input))
        } catch (e: Exception) {
            InteractorResult.Error(e)
        }
    }
}
