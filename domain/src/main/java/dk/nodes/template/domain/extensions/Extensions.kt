package dk.nodes.template.domain.extensions

import dk.nodes.template.domain.interactors.BaseAsyncInteractor
import dk.nodes.template.domain.interactors.InteractorResult

inline fun <T> T.guard(block: T.() -> Unit): T {
    if (this == null) block(); return this
}

fun <O: Any> BaseAsyncInteractor<O>.asResult(): ResultInteractor<O> {
    return ResultInteractor(this)
}


class ResultInteractor<O : Any>(private val interactor: BaseAsyncInteractor<O>) :
    BaseAsyncInteractor<InteractorResult<O>> {

    override suspend fun invoke(): InteractorResult<O> {
        return try {
            InteractorResult.Success(interactor())
        } catch (e: Exception) {
            InteractorResult.Error(e)
        }
    }
}