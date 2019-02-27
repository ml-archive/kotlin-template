package dk.eboks.app.domain.interactors.storebox

import dk.eboks.app.domain.models.local.ViewError
import dk.nodes.arch.domain.interactor.Interactor

interface DeleteStoreboxCreditCardInteractor : Interactor {
    var output: Output?
    var input: Input?

    data class Input(val id: String)

    interface Output {
        fun onDeleteCardSuccess(result: Boolean)
        fun onDeleteCardError(error: ViewError)
    }
}