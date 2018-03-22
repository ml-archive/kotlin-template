package dk.eboks.app.domain.interactors.message

import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.message.Content
import dk.eboks.app.domain.models.message.Message
import dk.nodes.arch.domain.interactor.Interactor

/**
 * Created by bison on 01/02/18.
 */
interface OpenAttachmentInteractor : Interactor {
    var output : Output?
    var input : Input?

    data class Input(var message : Message, var attachment : Content)

    interface Output {
        fun onOpenAttachment(attachment: Content, filename: String, mimeType : String)
        fun onOpenAttachmentError(error : ViewError)
    }
}