package dk.eboks.app.mail.domain.interactors.message.payment

import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.shared.Link
import dk.nodes.arch.domain.interactor.Interactor

interface GetPaymentLinkInteractor : Interactor {

    var input: Input?
    var output: Output?

    data class Input(val messageId: String, val folderId: Int, val type: String)

    interface Output {
        fun onPaymentLinkLoaded(link: Link)
        fun onPaymentLinkLoadingError(viewError: ViewError)
    }

}