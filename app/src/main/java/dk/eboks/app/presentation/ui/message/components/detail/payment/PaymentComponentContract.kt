package dk.eboks.app.presentation.ui.message.components.detail.payment

import dk.eboks.app.domain.models.message.Payment
import dk.nodes.arch.presentation.base.BasePresenter


interface PaymentComponentContract {

    interface View {
        fun showPaymentDetails(payment: Payment)
    }

    interface Presenter : BasePresenter<View> {

    }


}