package dk.eboks.app.domain.interactors.message.messageoperations

import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.message.Message
import dk.nodes.arch.domain.interactor.Interactor

/**
 * Created by bison on 01/02/18.
 */
interface MoveMessagesInteractor : Interactor {
    var output: Output?
    var input: Input?

    data class Input(var folderId: Int, var messageIds: ArrayList<Message>)

    interface Output {
        fun onMoveMessagesSuccess()
        fun onMoveMessagesError(error: ViewError)
    }
}