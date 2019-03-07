package dk.eboks.app.domain.interactors.storebox

import dk.eboks.app.domain.models.local.ViewError
import dk.nodes.arch.domain.interactor.Interactor

interface DeleteStoreboxReceiptInteractor : Interactor {
    var output: Output?
    var input: Input?

    data class Input(val id: String)

    interface Output {
        fun onDeleteReceiptSuccess()
        fun onDeleteReceiptError(error: ViewError)
    }
}