package dk.eboks.app.domain.interactors.sender

import dk.eboks.app.domain.models.local.ViewError
import dk.nodes.arch.domain.interactor.Interactor

interface GetSenderRegistrationLinkInteractor : Interactor {

    var input: Input?
    var output: Output?

    data class Input(val id: Long)
    interface Output {
        fun onLinkLoaded(link: String)
        fun onLinkLoadingError(viewError: ViewError)
    }

}