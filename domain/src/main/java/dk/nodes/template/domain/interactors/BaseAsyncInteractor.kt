package dk.nodes.template.domain.interactors

interface BaseAsyncInteractor<O> {
    suspend operator fun invoke(): O
}