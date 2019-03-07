package dk.eboks.app.mail.domain.interactors.message

import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.message.Content
import dk.eboks.app.domain.models.message.Message
import dk.nodes.arch.domain.interactor.Interactor

/**
 * Created by bison on 01/02/18.
 */
interface SaveAttachmentInteractor : Interactor {
    var output: Output?
    var input: Input?

    data class Input(var message: Message, var attachment: Content)

    interface Output {
        fun onSaveAttachment(filename: String)
        fun onSaveAttachmentError(error: ViewError)
    }
}