package dk.nodes.template.domain.interactors


interface BaseAsyncInteractor<O> {
    suspend fun run(): O
}