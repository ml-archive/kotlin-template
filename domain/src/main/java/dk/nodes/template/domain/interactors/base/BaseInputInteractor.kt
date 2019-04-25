package dk.nodes.template.domain.interactors.base

import dk.nodes.template.domain.interactors.InteractorResult

/**
 * Interactor with Input and Result.
 *
 * [I] input type of invoke function.
 *
 * [R] result type of invoke function.
 */
interface BaseInputInteractor<I, R> {

    val success get() = InteractorResult.Success(Unit)
    val error get() = InteractorResult.Error(Exception("Unknown interactor error"))

    suspend operator fun invoke(input: I): InteractorResult<R>

    fun success(data: R): InteractorResult.Success<R> {
        return InteractorResult.Success(data)
    }

    fun error(exception: Exception): InteractorResult.Error {
        return InteractorResult.Error(exception)
    }
}