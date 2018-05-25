package dk.eboks.app.domain.interactors.storebox

import dk.eboks.app.domain.models.channel.storebox.StoreboxCreditCard
import dk.eboks.app.domain.models.local.ViewError
import dk.nodes.arch.domain.interactor.Interactor

interface GetStoreboxCreditCardsInteractor : Interactor {
    var output: Output?

    interface Output {
        fun onGetCardsSuccessful(result: MutableList<StoreboxCreditCard>)
        fun onGetCardsError(error: ViewError)
    }
}