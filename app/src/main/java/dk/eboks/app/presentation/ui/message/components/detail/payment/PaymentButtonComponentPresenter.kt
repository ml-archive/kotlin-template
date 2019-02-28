package dk.eboks.app.presentation.ui.message.components.detail.payment

import androidx.lifecycle.Lifecycle
import dk.eboks.app.domain.interactors.message.payment.GetPaymentDetailsInteractor
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.message.payment.Payment
import dk.nodes.arch.presentation.base.BasePresenterImpl
import javax.inject.Inject

class PaymentButtonComponentPresenter @Inject constructor(
        private val appStateManager: AppStateManager,
        private val getPaymentDetailsInteractor: GetPaymentDetailsInteractor) :
        BasePresenterImpl<PaymentButtonComponentContract.View>(),
        PaymentButtonComponentContract.Presenter,
        GetPaymentDetailsInteractor.Output {

    override fun onViewCreated(view: PaymentButtonComponentContract.View, lifecycle: Lifecycle) {
        super.onViewCreated(view, lifecycle)
        getPaymentDetailsInteractor.output = this
        appStateManager.state?.currentMessage?.let {
            getPaymentDetailsInteractor.input = GetPaymentDetailsInteractor.Input(it.findFolderId(), it.id)
            getPaymentDetailsInteractor.run()
        }
    }

    override fun onPaymentDetailsLoaded(payment: Payment) {
        runAction { it.showPaymentDetails(payment) }
    }

    override fun onPaymentDetailsLoadingError(viewError: ViewError) {

    }
}