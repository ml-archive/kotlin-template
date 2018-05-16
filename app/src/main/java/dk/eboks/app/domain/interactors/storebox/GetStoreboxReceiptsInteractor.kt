package dk.eboks.app.domain.interactors.storebox

import dk.eboks.app.domain.models.channel.storebox.StoreboxReceiptItem
import dk.eboks.app.domain.models.local.ViewError
import dk.nodes.arch.domain.interactor.Interactor

interface GetStoreboxReceiptsInteractor : Interactor {
    var output: Output?

    interface Output {
        fun onGetReceipts(messages: List<StoreboxReceiptItem>)
        fun onGetReceiptsError(error: ViewError)
    }
}