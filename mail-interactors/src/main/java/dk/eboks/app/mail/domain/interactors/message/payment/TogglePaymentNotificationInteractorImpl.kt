package dk.eboks.app.mail.domain.interactors.message.payment

import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.network.Api
import dk.eboks.app.util.exceptionToViewError
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor
import timber.log.Timber
import java.lang.Exception
import javax.inject.Inject

internal class TogglePaymentNotificationInteractorImpl @Inject constructor(
        executor: Executor,
        private val api: Api)
    : BaseInteractor(executor), TogglePaymentNotificationInteractor {

    override var input: TogglePaymentNotificationInteractor.Input? = null
    override var output: TogglePaymentNotificationInteractor.Output? = null
    override fun execute() {
        input?.let {
            try {
                val result = api.togglePaymentNotifications(it.folderId, it.messageId, it.on).execute()
                Timber.d("${result.isSuccessful}")
                if (result.isSuccessful) runOnUIThread { output?.onNotificationsToggleUpdated(it.on) }
                else runOnUIThread { output?.onNotificationToggleUpdateError(ViewError()) }
            } catch (exception: Exception) {
                runOnUIThread { output?.onNotificationToggleUpdateError(exceptionToViewError(exception)) }
            }
        }
    }


}