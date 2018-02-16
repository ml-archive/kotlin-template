package dk.eboks.app.domain.interactors.message

import dk.eboks.app.domain.models.Message
import dk.nodes.arch.domain.interactor.Interactor

/**
 * Created by bison on 01/02/18.
 */
interface OpenMessageInteractor : Interactor {
    var output : Output?
    var input : Input?

    data class Input(val cached: Boolean, var msg : Message)

    interface Output {
        fun onOpenMessage(messages : List<Message>)
        fun onOpenMessageLocked()
        fun onOpenMessagePromulgated()
        fun onOpenMessageProtected()
        fun onOpenMessageError(msg : String)
    }
}