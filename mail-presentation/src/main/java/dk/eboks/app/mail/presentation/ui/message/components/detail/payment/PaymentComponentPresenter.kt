package dk.eboks.app.mail.presentation.ui.message.components.detail.payment

import androidx.lifecycle.Lifecycle
import dk.eboks.app.mail.domain.interactors.message.payment.GetPaymentLinkInteractor
import dk.eboks.app.mail.domain.interactors.message.payment.TogglePaymentNotificationInteractor
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.message.payment.PaymentOption
import dk.eboks.app.domain.models.shared.Link
import dk.nodes.arch.presentation.base.BasePresenterImpl
import javax.inject.Inject

class PaymentComponentPresenter @Inject constructor(
        private val appStateManager: AppStateManager,
        private val getPaymentLinkInteractor: GetPaymentLinkInteractor,
        private val togglePaymentNotificationInteractor: TogglePaymentNotificationInteractor)
    :   BasePresenterImpl<PaymentComponentContract.View>(),
        PaymentComponentContract.Presenter,
        GetPaymentLinkInteractor.Output,
        TogglePaymentNotificationInteractor.Output {


    override fun onViewCreated(view: PaymentComponentContract.View, lifecycle: Lifecycle) {
        super.onViewCreated(view, lifecycle)
        getPaymentLinkInteractor.output = this
        togglePaymentNotificationInteractor.output = this
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

    override fun toggleNotifications(newValue: Boolean) {
        appStateManager.state?.currentMessage?.let { message ->
            togglePaymentNotificationInteractor.input = TogglePaymentNotificationInteractor.Input(message.findFolderId(), message.id, newValue)
            togglePaymentNotificationInteractor.run()
        }
    }

    override fun onNotificationsToggleUpdated(newValue: Boolean) {
    }

    override fun onNotificationToggleUpdateError(viewError: ViewError) {

    }
}