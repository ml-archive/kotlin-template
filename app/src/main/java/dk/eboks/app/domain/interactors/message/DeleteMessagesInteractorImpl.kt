package dk.eboks.app.domain.interactors.message

import dk.eboks.app.domain.repositories.MessagesRepository
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor

class DeleteMessagesInteractorImpl(executor: Executor) :
        BaseInteractor(executor),
        DeleteMessagesInteractor {
    override var output: DeleteMessagesInteractor.Output? = null
    override var input: DeleteMessagesInteractor.Input? = null


    override fun execute() {
        // Todo implement delete route?
        Thread.sleep(1000)
        output?.onDeleteMessagesSuccess()
    }
}