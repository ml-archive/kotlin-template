package dk.eboks.app.domain.interactors.message

import dk.eboks.app.domain.repositories.MessagesRepository
import dk.eboks.app.domain.repositories.RepositoryException
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor

/**
 * Created by bison on 01/02/18.
 */
class OpenMessageInteractorImpl(executor: Executor, val messagesRepository: MessagesRepository) : BaseInteractor(executor), OpenMessageInteractor {
    override var output: OpenMessageInteractor.Output? = null
    override var input: OpenMessageInteractor.Input? = null

    override fun execute() {
        try {
            runOnUIThread {

            }
        } catch (e: RepositoryException) {
            runOnUIThread {

            }
        }
    }
}