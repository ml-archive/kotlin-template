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


    data class Input(val cached: Boolean, var folder: Folder? = null, var sender: Sender? = null, var offset : Int = 0, var limit : Int = 20)

    interface Output {
        fun onGetMessages(messages : List<Message>)
        fun onGetMessagesError(error : ViewError)
    }
}