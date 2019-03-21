package dk.eboks.app.mail.domain.interactors.senders

import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.sender.Sender
import dk.nodes.arch.domain.interactor.Interactor

/**
 * Created by bison on 01/02/18.
 */
interface GetSendersInteractor : Interactor {
    var output: Output?
    var input: Input?

    data class Input(
        val cached: Boolean = true,
        val name: String = "",
        val userId: Int?,
        val id: Long = 0
    )

    interface Output {
        fun onGetSenders(senders: List<Sender>)
        fun onGetSendersError(error: ViewError)
    }
}