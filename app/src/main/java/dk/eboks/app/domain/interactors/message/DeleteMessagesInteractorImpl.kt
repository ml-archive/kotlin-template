package dk.eboks.app.domain.interactors.message

import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.repositories.MessagesRepository
import dk.eboks.app.util.exceptionToViewError
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor
import timber.log.Timber

class DeleteMessagesInteractorImpl(executor: Executor, val messagesRepository: MessagesRepository) :
        BaseInteractor(executor),
        DeleteMessagesInteractor {
    override var output: DeleteMessagesInteractor.Output? = null
    override var input: DeleteMessagesInteractor.Input? = null


    override fun execute() {
        try {
            input?.message?.let {
                val result = messagesRepository.deleteMessage(it.findFolderId(),it.id)
                runOnUIThread {
                    output?.onDeleteMessagesSuccess()
                }
            }
        } catch (t: Throwable) {
            runOnUIThread {
                output?.onDeleteMessagesError(ViewError())
            }
        }
    }
}