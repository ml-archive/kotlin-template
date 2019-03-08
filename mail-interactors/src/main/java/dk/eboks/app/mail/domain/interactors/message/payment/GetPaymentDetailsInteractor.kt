package dk.eboks.app.mail.domain.interactors.message.payment

import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.message.payment.Payment
import dk.nodes.arch.domain.interactor.Interactor

interface GetPaymentDetailsInteractor : Interactor {

    var input: Input?
    var output: Output?

    data class Input(val folderId: Int, val messageId: String)

    interface Output {
        fun onPaymentDetailsLoaded(payment: Payment)
        fun onPaymentDetailsLoadingError(viewError: ViewError)
    }
}