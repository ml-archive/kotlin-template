package dk.eboks.app.domain.interactors.sender

import dk.eboks.app.domain.exceptions.RepositoryException
import dk.eboks.app.domain.repositories.SenderCategoriesRepository
import dk.eboks.app.domain.repositories.SendersRepository
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor
import timber.log.Timber

/**
* Created by bison on 01/02/18.
* @author   bison
* @since    01/02/18.
*/
class GetSenderDetailInteractorImpl(executor: Executor, val sendersRepository: SendersRepository) : BaseInteractor(executor), GetSenderDetailInteractor {

    override var output: GetSenderDetailInteractor.Output? = null
    override var input: GetSenderDetailInteractor.Input? = null


    override fun execute() {
        try {
            val senders = sendersRepository.getSenderDetail(input?.id ?: 0)
            runOnUIThread {
                output?.onGetSender(senders)
            }
        } catch (e: RepositoryException) {
            runOnUIThread {
                output?.onGetSenderError(e.message ?: "Unknown error")
            }
        }
    }
}