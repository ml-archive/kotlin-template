package dk.nodes.template.domain.interactors

import androidx.lifecycle.LiveData

interface LiveDataInteractor<Input : Any, Output> {
    fun data(): LiveData<Output>
    suspend fun run(input: Input)
}