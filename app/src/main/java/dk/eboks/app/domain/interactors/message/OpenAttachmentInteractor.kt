package dk.eboks.app.domain.interactors.message

import dk.eboks.app.domain.models.Content
import dk.nodes.arch.domain.interactor.Interactor

/**
 * Created by bison on 01/02/18.
 */
interface OpenAttachmentInteractor : Interactor {
    var output : Output?
    var input : Input?

    data class Input(var attachment : Content)

    interface Output {
        fun onOpenAttachment(filename: String, mimeType : String)
        fun onOpenAttachmentError(msg : String)
    }
}