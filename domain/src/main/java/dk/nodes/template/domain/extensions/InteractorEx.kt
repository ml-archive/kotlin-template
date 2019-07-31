package dk.nodes.template.domain.extensions

import dk.nodes.template.domain.interactors.InteractorResult

fun <T, R> InteractorResult<T>.ifSuccess(block: (T) -> R): InteractorResult<T> {
    if (this is InteractorResult.Success) {
        block(this.data)
    }
    return this
}

fun <T, R> InteractorResult<T>.ifError(block: (Exception) -> R): InteractorResult<T> {
    if (this is InteractorResult.Error) {
        block(this.exception)
    }
    return this
}

fun <T> InteractorResult<T>.successDataOrNull(): T? =
        if (this is InteractorResult.Success) this.data else null