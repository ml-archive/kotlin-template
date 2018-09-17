package dk.eboks.app.domain.interactors.storebox

import dk.eboks.app.domain.models.local.ViewError
import dk.nodes.arch.domain.interactor.Interactor

interface SaveReceiptInteractor: Interactor {
    var output : Output?
    var input : Input?

    data class Input(val receiptId: String, val folderId : Int)

    interface Output {
        fun onSaveReceiptSuccess()
        fun onSaveReceiptError(error : ViewError)
    }
}