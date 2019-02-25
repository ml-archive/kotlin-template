package dk.eboks.app.mail.domain.interactors.message

import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.message.Message
import dk.nodes.arch.domain.interactor.Interactor

/**
 * Created by bison on 01/02/18.
 */
interface GetLatestUploadsInteractor : Interactor {
    var output: Output?
    var input: Input?

    data class Input(var offset: Int = 0, var limit: Int = 5)

    interface Output {
        fun onGetLatestUploads(messages: List<Message>)
        fun onGetLatestUploadsError(error: ViewError)
    }
}