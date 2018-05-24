package dk.eboks.app.domain.interactors.message

import dk.eboks.app.domain.models.folder.Folder
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.message.Message
import dk.eboks.app.domain.models.sender.Sender
import dk.nodes.arch.domain.interactor.Interactor

/**
 * Created by bison on 01/02/18.
 */
interface GetMessagesInteractor : Interactor {
    var output : Output?
    var input : Input?


    data class Input(val cached: Boolean, var folder: Folder? = null, var sender: Sender? = null)

    interface Output {
        fun onGetMessages(messages : List<Message>)
        fun onGetMessagesError(error : ViewError)
    }

    /* Dont know if were gonna go down this road yet or let it still try to match api call to foldertype
    enum class MessageCategory {
        HIGHLIGHTS, LATEST, UNREAD, UPLOADS
    }
    */
}