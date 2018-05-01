package dk.eboks.app.domain.interactors.message

import dk.eboks.app.domain.models.local.ViewError
import dk.nodes.arch.domain.interactor.Interactor

/**
 * Created by bison on 01/02/18.
 */
interface MoveMessagesInteractor : Interactor {
    var output: Output?
    var input: Input?

    data class Input(var folderName: String, var messageIds: ArrayList<String>)

    interface Output {
        fun onMoveMessagesSuccess()
        fun onMoveMessagesError(error: ViewError)
    }
}