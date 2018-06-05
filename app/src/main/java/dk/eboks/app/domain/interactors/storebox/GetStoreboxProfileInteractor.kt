package dk.eboks.app.domain.interactors.storebox

import dk.eboks.app.domain.models.channel.storebox.StoreboxProfile
import dk.eboks.app.domain.models.local.ViewError
import dk.nodes.arch.domain.interactor.Interactor

interface GetStoreboxProfileInteractor : Interactor {
    var output: Output?

    interface Output {
        fun onGetProfile(result: StoreboxProfile)
        fun onGetProfileError(error: ViewError)
    }
}