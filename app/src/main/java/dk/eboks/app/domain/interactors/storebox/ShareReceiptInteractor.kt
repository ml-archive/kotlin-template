package dk.eboks.app.domain.interactors.storebox

import dk.eboks.app.domain.models.local.ViewError
import dk.nodes.arch.domain.interactor.Interactor

interface ShareReceiptInteractor: Interactor {
    var output : Output?
    var input : Input?

    data class Input(val receiptId: String)

    interface Output {
        fun onShareReceiptSuccess(filename : String)
        fun onShareReceiptError(error : ViewError)
    }
}