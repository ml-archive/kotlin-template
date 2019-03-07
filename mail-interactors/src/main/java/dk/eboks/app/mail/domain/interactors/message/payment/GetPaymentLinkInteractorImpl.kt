package dk.eboks.app.mail.domain.interactors.message.payment

import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.repositories.MessagesRepository
import dk.eboks.app.util.exceptionToViewError
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor
import javax.inject.Inject

internal class GetPaymentLinkInteractorImpl @Inject constructor(executor: Executor,
                                   private val repository: MessagesRepository)
    : BaseInteractor(executor),
        GetPaymentLinkInteractor {

    override var input: GetPaymentLinkInteractor.Input? = null
    override var output: GetPaymentLinkInteractor.Output? = null

    override fun execute() {
        input?.let {
            try {
                val result = repository.getPaymentLink(it.folderId, it.messageId, it.type)
                if (result == null) {
                    runOnUIThread { output?.onPaymentLinkLoadingError(ViewError()) }
                } else {
                    output?.onPaymentLinkLoaded(result)
                }
            } catch (e: Exception) {
                output?.onPaymentLinkLoadingError(exceptionToViewError(e))
            }

        }
    }


}