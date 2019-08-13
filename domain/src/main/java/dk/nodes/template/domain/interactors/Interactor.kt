package dk.nodes.template.domain.interactors

interface Interactor<I, O> {
    suspend operator fun invoke(input: I): O
}

suspend operator fun <O> Interactor<Unit, O>.invoke() = invoke(Unit)