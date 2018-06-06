package dk.eboks.app.domain.interactors.storebox

import dk.eboks.app.domain.models.channel.ChannelFlags
import dk.eboks.app.domain.models.local.ViewError
import dk.nodes.arch.domain.interactor.Interactor

interface UpdateStoreboxFlagsInteractor: Interactor {
    var output : Output?
    var input : Input?

    data class Input(val flags : ChannelFlags)

    interface Output {
        fun onUpdateFlagsSuccess()
        fun onUpdateFlagsError(error : ViewError)
    }
}