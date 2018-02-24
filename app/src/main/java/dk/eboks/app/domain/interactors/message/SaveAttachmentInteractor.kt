package dk.eboks.app.domain.interactors.message

import dk.eboks.app.domain.models.Content
import dk.eboks.app.domain.models.Message
import dk.nodes.arch.domain.interactor.Interactor

/**
 * Created by bison on 01/02/18.
 */
interface SaveAttachmentInteractor : Interactor {
    var output : Output?
    var input : Input?

    data class Input(var message : Message, var attachment : Content)

    interface Output {
        fun onSaveAttachment(filename: String)
        fun onSaveAttachmentError(msg : String)
    }
}