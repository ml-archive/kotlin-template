package dk.nodes.template.domain.interactors

interface BaseAsyncInteractor<O: Any> {
    suspend operator fun invoke(): IResult<O>
}