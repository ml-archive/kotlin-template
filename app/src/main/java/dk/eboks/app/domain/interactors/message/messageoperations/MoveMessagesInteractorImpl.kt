package dk.eboks.app.domain.interactors.message.messageoperations

import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.message.MessagePatch
import dk.eboks.app.domain.repositories.MessagesRepository
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor
import javax.inject.Inject

class MoveMessagesInteractorImpl @Inject constructor(
    executor: Executor,
    private val messagesRepository: MessagesRepository
) :
    BaseInteractor(executor),
    MoveMessagesInteractor {
    override var output: MoveMessagesInteractor.Output? = null
    override var input: MoveMessagesInteractor.Input? = null

    override fun execute() {
        try {
            input?.folderId?.let { folderId ->
                input?.messageIds?.let { messageIds ->
                    for (msg in messageIds) {
                        val messagePatch = MessagePatch(folderId = folderId)
                        messagesRepository.updateMessage(msg, messagePatch)
                        runOnUIThread {
                            output?.onMoveMessagesSuccess()
                        }
                    }
                }
            }
        } catch (t: Throwable) {
            runOnUIThread {
                output?.onMoveMessagesError(ViewError())
            }
        }
    }
}