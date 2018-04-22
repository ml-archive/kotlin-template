package dk.eboks.app.domain.interactors.storebox

import dk.eboks.app.domain.models.channel.storebox.StoreboxReceipt
import dk.eboks.app.domain.models.local.ViewError
import dk.nodes.arch.domain.interactor.Interactor

interface GetStoreboxReceiptInteractor : Interactor {
    var input: Input?
    var output: Output?

    data class Input(val storeboxId: String)

    interface Output {
        fun onGetReceipt(storeboxReceipt: StoreboxReceipt)
        fun onGetReceiptsError(error: ViewError)
    }
}