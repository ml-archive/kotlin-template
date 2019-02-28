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

    companion object {
        const val NO_PRIVATE_SENDER_WARNING = 9100
        const val MANDATORY_OPEN_RECEIPT = 12194
        const val VOLUNTARY_OPEN_RECEIPT = 12245
        const val MESSAGE_QUARANTINED = 9300
        const val MESSAGE_RECALLED = 9301
        const val MESSAGE_LOCKED = 9302
        const val PROMULGATION = 12260
    }
}
