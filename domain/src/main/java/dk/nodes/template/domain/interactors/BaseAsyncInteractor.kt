package dk.nodes.template.domain.interactors

import kotlinx.coroutines.channels.ReceiveChannel

interface BaseAsyncInteractor<O: Any> {
    suspend operator fun invoke(): IResult<O>
    suspend fun receive(): ReceiveChannel<O>
}