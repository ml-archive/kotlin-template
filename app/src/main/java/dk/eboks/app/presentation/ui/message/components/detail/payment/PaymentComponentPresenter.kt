package dk.eboks.app.presentation.ui.message.components.detail.payment

import androidx.lifecycle.Lifecycle
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.message.Payment
import dk.nodes.arch.presentation.base.BasePresenter
import dk.nodes.arch.presentation.base.BasePresenterImpl
import javax.inject.Inject

class PaymentComponentPresenter @Inject constructor(val appStateManager: AppStateManager)
    : BasePresenterImpl<PaymentComponentContract.View>(), PaymentComponentContract.Presenter {


    override fun onViewCreated(view: PaymentComponentContract.View, lifecycle: Lifecycle) {
        super.onViewCreated(view, lifecycle)
        appStateManager.state?.currentMessage?.payment?.let { payment ->
            runAction { it.showPaymentDetails(payment) }
        }

    }

}