package dk.eboks.app.presentation.ui.message.components.detail.payment

import androidx.lifecycle.Lifecycle
import dk.eboks.app.domain.interactors.message.payment.GetPaymentLinkInteractor
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.message.payment.Payment
import dk.eboks.app.domain.models.message.payment.PaymentOption
import dk.eboks.app.domain.models.shared.Link
import dk.nodes.arch.presentation.base.BasePresenterImpl
import javax.inject.Inject

class PaymentComponentPresenter @Inject constructor(
        private val appStateManager: AppStateManager,
        private val getPaymentLinkInteractor: GetPaymentLinkInteractor)
    :   BasePresenterImpl<PaymentComponentContract.View>(),
        PaymentComponentContract.Presenter,
        GetPaymentLinkInteractor.Output {


    override fun onViewCreated(view: PaymentComponentContract.View, lifecycle: Lifecycle) {
        super.onViewCreated(view, lifecycle)
        getPaymentLinkInteractor.output = this
    }

    override fun loadPaymentLink(option: PaymentOption) {
        appStateManager.state?.currentMessage?.let { message ->
            getPaymentLinkInteractor.input = GetPaymentLinkInteractor.Input(message.id, message.findFolderId(), option.type)
            getPaymentLinkInteractor.run()
        }
    }

    override fun onPaymentLinkLoaded(link: Link) {
        runAction { it.showPaymentWebView(link) }
    }

    override fun onPaymentLinkLoadingError(viewError: ViewError) {
    }
}