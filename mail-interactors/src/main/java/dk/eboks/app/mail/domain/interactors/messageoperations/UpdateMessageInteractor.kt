package dk.eboks.app.mail.domain.interactors.messageoperations

import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.message.Message
import dk.eboks.app.domain.models.message.MessagePatch
import dk.nodes.arch.domain.interactor.Interactor

interface UpdateMessageInteractor : Interactor {
    var output: Output?
    var input: Input?

    data class Input(var messages: ArrayList<Message>, var messagePatch: MessagePatch)

    interface Output {
        fun onUpdateMessageSuccess()
        fun onUpdateMessageError(error: ViewError)
    }
}