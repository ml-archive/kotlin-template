package dk.eboks.app.domain.interactors.message

import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.message.Message
import dk.eboks.app.domain.models.message.MessagePatch
import dk.nodes.arch.domain.interactor.Interactor

interface UpdateMessageInteractor : Interactor {
    var output: Output?
    var input: Input?

    data class Input(var message: Message, var messagePatch: MessagePatch)

    interface Output {
        fun onUpdateMessageSuccess()
        fun onUpdateMessageError(error: ViewError)
    }
}