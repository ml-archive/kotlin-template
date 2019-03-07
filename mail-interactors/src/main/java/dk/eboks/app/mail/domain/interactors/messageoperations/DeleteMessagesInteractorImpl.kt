package dk.eboks.app.mail.domain.interactors.messageoperations

import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.repositories.MessagesRepository
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor
import javax.inject.Inject

internal class DeleteMessagesInteractorImpl @Inject constructor(
    executor: Executor,
    private val messagesRepository: MessagesRepository
) :
    BaseInteractor(executor),
        DeleteMessagesInteractor {
    override var output: DeleteMessagesInteractor.Output? = null
    override var input: DeleteMessagesInteractor.Input? = null

    override fun execute() {
        try {
            input?.messages?.let { messages ->
                for (msg in messages) {
                    messagesRepository.deleteMessage(msg.findFolderId(), msg.id)
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