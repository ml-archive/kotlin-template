package dk.eboks.app.domain.interactors.message

import dk.eboks.app.domain.models.formreply.ReplyForm
import dk.eboks.app.domain.models.local.ViewError
import dk.nodes.arch.domain.interactor.Interactor

/**
 * Created by bison on 01/02/18.
 */
interface GetReplyFormInteractor : Interactor {
    var output : Output?
    var input : Input?

    data class Input(var messageId: String, var folderId: Long)

    interface Output {
        fun onGetReplyForm(form : ReplyForm)
        fun onGetReplyFormError(error : ViewError)
    }
}