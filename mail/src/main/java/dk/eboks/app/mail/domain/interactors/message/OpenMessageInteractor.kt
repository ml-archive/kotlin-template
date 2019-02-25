package dk.eboks.app.mail.domain.interactors.message

import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.message.Message
import dk.eboks.app.domain.models.protocol.ServerError
import dk.nodes.arch.domain.interactor.Interactor

/**
 * Created by bison on 01/02/18.
 */
interface OpenMessageInteractor : Interactor {
    var output: Output?
    var input: Input?

    data class Input(var msg: Message)

    interface Output {
        fun onOpenMessageDone()
        fun onOpenMessageServerError(serverError: ServerError)
        fun onOpenMessageError(error: ViewError)
        fun onReAuthenticate(loginProviderId: String, msg: Message)
        fun onPrivateSenderWarning(msg: Message)
        fun isViewAttached(): Boolean
    }
}