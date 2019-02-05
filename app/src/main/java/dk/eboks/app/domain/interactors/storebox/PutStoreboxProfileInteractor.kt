package dk.eboks.app.domain.interactors.storebox

import dk.eboks.app.domain.models.channel.storebox.StoreboxProfile
import dk.eboks.app.domain.models.local.ViewError
import dk.nodes.arch.domain.interactor.Interactor

interface PutStoreboxProfileInteractor : Interactor {
    var output: Output?
    var input: Input?

    data class Input(val profile: StoreboxProfile)

    interface Output {
        fun onPutProfile()
        fun onPutProfileError(error: ViewError)
    }
}