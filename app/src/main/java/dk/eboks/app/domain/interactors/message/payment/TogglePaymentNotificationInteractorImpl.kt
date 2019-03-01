package dk.eboks.app.domain.interactors.message.payment

import dk.eboks.app.network.Api
import dk.eboks.app.util.exceptionToViewError
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor
import timber.log.Timber
import java.lang.Exception

class TogglePaymentNotificationInteractorImpl(
        executor: Executor,
        private val api: Api)
    : BaseInteractor(executor), TogglePaymentNotificationInteractor {

    override var input: TogglePaymentNotificationInteractor.Input? = null
    override var ouput: TogglePaymentNotificationInteractor.Output? = null
    override fun execute() {
        input?.let {
            try {
                val result = api.togglePaymentNotifications(it.folderId, it.messageId, it.on).execute()
                Timber.d("${result.isSuccessful}")
                if (result.isSuccessful) runOnUIThread { ouput?.onNotificationsToggleUpdated(it.on) }
            } catch (exception: Exception) {
                runOnUIThread { ouput?.onNotificationToggleUpdateError(exceptionToViewError(exception)) }
            }
        }
    }


}