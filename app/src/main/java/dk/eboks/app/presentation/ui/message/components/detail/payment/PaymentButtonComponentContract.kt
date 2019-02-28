package dk.eboks.app.presentation.ui.message.components.detail.payment

import dk.eboks.app.domain.models.message.payment.Payment
import dk.nodes.arch.presentation.base.BasePresenter
import dk.nodes.arch.presentation.base.BaseView

interface PaymentButtonComponentContract {

    interface Presenter : BasePresenter<View>

    interface View : BaseView {
        fun showPaymentDetails(payment: Payment)
    }

}