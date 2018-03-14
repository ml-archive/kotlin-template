package dk.eboks.app.domain.interactors.message

import dk.eboks.app.domain.models.folder.Folder
import dk.eboks.app.domain.models.message.Message
import dk.nodes.arch.domain.interactor.Interactor

/**
 * Created by bison on 01/02/18.
 */
interface GetMessagesInteractor : Interactor {
    var output : Output?
    var input : Input?

    data class Input(val cached: Boolean, var folder: Folder)

    interface Output {
        fun onGetMessages(messages : List<Message>)
        fun onGetMessagesError(msg : String)
    }
}