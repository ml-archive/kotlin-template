package dk.eboks.app.domain.interactors

import dk.eboks.app.domain.models.Message
import dk.eboks.app.domain.models.Sender
import dk.nodes.arch.domain.interactor.Interactor

/**
 * Created by bison on 01/02/18.
 */
interface GetMessagesInteractor : Interactor {
    var output : Output?
    var input : Input?

    data class Input(val cached: Boolean, var folderId : Long)

    interface Output {
        fun onGetMessages(messages : List<Message>)
        fun onGetMessagesError(msg : String)
    }
}