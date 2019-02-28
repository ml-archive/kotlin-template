package dk.eboks.app.presentation.ui.message.components.detail.payment

import dk.eboks.app.domain.models.message.payment.Payment
import dk.eboks.app.domain.models.message.payment.PaymentOption
import dk.eboks.app.domain.models.shared.Link
import dk.nodes.arch.presentation.base.BasePresenter


interface PaymentComponentContract {

    interface View {
        fun showPaymentDetails(payment: Payment)
        fun showPaymentWebView(link: Link)
    }

    interface Presenter : BasePresenter<View> {
        fun loadPaymentLink(option: PaymentOption)

    }


}