package dk.nodes.template.domain.interactors.base

import dk.nodes.template.domain.interactors.InteractorResult

/**
 * Interactor with Result, and no Input.
 *
 * [R] result type of invoke function.
 */
interface BaseInteractor<R> {

    suspend fun invoke(): InteractorResult<R>

    fun success(data: R): InteractorResult.Success<R> {
        return InteractorResult.Success(data)
    }

    fun error(exception: Exception): InteractorResult.Error {
        return InteractorResult.Error(exception)
    }
}