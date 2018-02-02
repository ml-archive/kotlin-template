package dk.eboks.app.domain.interactors

import dk.eboks.app.domain.repositories.MessagesRepository
import dk.eboks.app.domain.repositories.RepositoryException
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor

/**
 * Created by bison on 01/02/18.
 */
class GetMessagesInteractorImpl(executor: Executor, val messagesRepository: MessagesRepository) : BaseInteractor(executor), GetMessagesInteractor {
    override var output: GetMessagesInteractor.Output? = null
    override var input: GetMessagesInteractor.Input? = null

    override fun execute() {
        try {
            val messages = messagesRepository.getMessages(input?.cached ?: true, input?.folderId ?: 0)
            runOnUIThread {
                output?.onGetMessages(messages)
            }
        } catch (e: RepositoryException) {
            runOnUIThread {
                output?.onGetMessagesError(e.message ?: "Unknown error")
            }
        }
    }
}