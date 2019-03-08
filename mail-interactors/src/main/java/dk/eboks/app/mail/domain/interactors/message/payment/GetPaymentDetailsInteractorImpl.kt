package dk.eboks.app.mail.domain.interactors.message.payment

import dk.eboks.app.domain.repositories.MessagesRepository
import dk.eboks.app.util.exceptionToViewError
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor
import java.lang.Exception
import javax.inject.Inject

internal class GetPaymentDetailsInteractorImpl @Inject constructor(
    executor: Executor,
    private val repository: MessagesRepository
)
    : BaseInteractor(executor),
        GetPaymentDetailsInteractor {

    override var input: GetPaymentDetailsInteractor.Input? = null
    override var output: GetPaymentDetailsInteractor.Output? = null

    override fun execute() {
        input?.let {
            try {
                val result = repository.getPaymentDetails(it.folderId, it.messageId)
                runOnUIThread { output?.onPaymentDetailsLoaded(result) }
            } catch (e: Exception) {
                runOnUIThread { output?.onPaymentDetailsLoadingError(exceptionToViewError(e)) }
            }
        }
    }
}