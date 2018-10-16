package dk.eboks.app.domain.interactors.message.messageoperations

import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.repositories.MessagesRepository
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor

class DeleteMessagesInteractorImpl(executor: Executor, val messagesRepository: MessagesRepository) :
        BaseInteractor(executor),
        DeleteMessagesInteractor {
    override var output: DeleteMessagesInteractor.Output? = null
    override var input: DeleteMessagesInteractor.Input? = null


    override fun execute() {
        try {
            input?.messages?.let { messages ->
                for (msg in messages) {
                    messagesRepository.deleteMessage(msg.folderId, msg.id)
                }
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