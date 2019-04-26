package dk.nodes.template.domain.interactors

interface BaseAsyncInteractor<I, O> {
    suspend operator fun invoke(input: I): O
}