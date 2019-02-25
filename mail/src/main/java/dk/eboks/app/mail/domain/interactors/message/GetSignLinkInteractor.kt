package dk.eboks.app.mail.domain.interactors.message

import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.message.Message
import dk.eboks.app.domain.models.shared.Link
import dk.nodes.arch.domain.interactor.Interactor

interface GetSignLinkInteractor : Interactor {
    var output: Output?
    var input: Input?

    data class Input(var msg: Message)

    interface Output {
        fun onGetSignLink(result: Link)
        fun onGetSignLinkError(error: ViewError)
    }
}