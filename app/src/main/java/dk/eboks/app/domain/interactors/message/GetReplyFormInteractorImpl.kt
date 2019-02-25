package dk.eboks.app.domain.interactors.message

import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.repositories.MessagesRepository
import dk.eboks.app.mail.domain.interactors.message.GetReplyFormInteractor
import dk.eboks.app.util.exceptionToViewError
import dk.eboks.app.util.guard
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor
import timber.log.Timber

class GetReplyFormInteractorImpl(executor: Executor, val messagesRepository: MessagesRepository) :
    BaseInteractor(executor), GetReplyFormInteractor {
    override var output: GetReplyFormInteractor.Output? = null
    override var input: GetReplyFormInteractor.Input? = null

    override fun execute() {
        try {
            input?.let {
                val result = messagesRepository.getMessageReplyForm(it.folderId, it.messageId)
                runOnUIThread {
                    output?.onGetReplyForm(result)
                }
            }.guard {
                runOnUIThread {
                    output?.onGetReplyFormError(ViewError())
                }
            }
        } catch (t: Throwable) {
            Timber.e(t)
            runOnUIThread {
                output?.onGetReplyFormError(exceptionToViewError(t, shouldClose = true))
            }
        }
    }
}