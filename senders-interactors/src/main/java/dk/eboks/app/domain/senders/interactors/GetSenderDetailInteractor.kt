package dk.eboks.app.domain.senders.interactors

import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.sender.Sender
import dk.nodes.arch.domain.interactor.Interactor

/**
 * Created by chnt on 01/02/18.
 * @author chnt
 * @since 01/02/18.
 */
interface GetSenderDetailInteractor : Interactor {
    var output: Output?
    var input: Input?

    data class Input(val id: Long = 0)

    interface Output {
        fun onGetSender(senders: Sender)
        fun onGetSenderError(error: ViewError)
    }
}