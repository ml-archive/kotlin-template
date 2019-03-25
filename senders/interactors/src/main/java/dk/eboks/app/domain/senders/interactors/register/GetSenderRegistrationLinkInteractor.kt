package dk.eboks.app.domain.senders.interactors.register

import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.shared.Link
import dk.nodes.arch.domain.interactor.Interactor

interface GetSenderRegistrationLinkInteractor : Interactor {

    var input: Input?
    var output: Output?

    data class Input(val id: Long)
    interface Output {
        fun onLinkLoaded(link: Link)
        fun onLinkLoadingError(viewError: ViewError)
    }
}