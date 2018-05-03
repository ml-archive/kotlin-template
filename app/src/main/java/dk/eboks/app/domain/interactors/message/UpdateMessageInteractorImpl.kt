package dk.eboks.app.domain.interactors.message

import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor

class UpdateMessageInteractorImpl(executor: Executor) : BaseInteractor(executor),
                                                        UpdateMessageInteractor {
    override var input: UpdateMessageInteractor.Input? = null
    override var output: UpdateMessageInteractor.Output? = null


    override fun execute() {
        // TODO implement update endpoint
        Thread.sleep(1000)

        runOnUIThread {
            output?.onUpdateMessageSuccess()
        }
    }
}