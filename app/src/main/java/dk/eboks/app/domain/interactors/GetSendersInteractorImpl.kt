package dk.eboks.app.domain.interactors

import dk.eboks.app.domain.repositories.RepositoryException
import dk.eboks.app.domain.repositories.SendersRepository
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor

/**
 * Created by bison on 01/02/18.
 */
class GetSendersInteractorImpl(executor: Executor, val sendersRepository: SendersRepository) : BaseInteractor(executor), GetSendersInteractor {
    override var output: GetSendersInteractor.Output? = null
    override var input: GetSendersInteractor.Input? = null

    override fun execute() {
        try {
            val senders = sendersRepository.getSenders(input?.cached ?: true)
            runOnUIThread {
                output?.onGetSenders(senders)
            }
        } catch (e: RepositoryException) {
            runOnUIThread {
                output?.onGetSendersError(e.message ?: "Unknown error")
            }
        }
    }
}