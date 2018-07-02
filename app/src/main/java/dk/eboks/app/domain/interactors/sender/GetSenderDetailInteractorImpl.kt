package dk.eboks.app.domain.interactors.sender

import dk.eboks.app.domain.repositories.SendersRepository
import dk.eboks.app.util.exceptionToViewError
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor

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
        } catch (t: Throwable) {
            runOnUIThread {
                output?.onGetSenderError(exceptionToViewError(t))
            }
        }
    }
}