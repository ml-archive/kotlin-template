package dk.eboks.app.mail.domain.interactors.message

import dk.eboks.app.domain.models.formreply.ReplyForm
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.message.Message
import dk.nodes.arch.domain.interactor.Interactor

/**
 * Created by bison on 01/02/18.
 */
interface SubmitReplyFormInteractor : Interactor {
    var output: Output?
    var input: Input?

    data class Input(var msg: Message, var form: ReplyForm)

    interface Output {
        fun onSubmitReplyForm()
        fun onSubmitReplyFormError(error: ViewError)
    }
}