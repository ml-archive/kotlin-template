package dk.eboks.app.domain.interactors.message

import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor

class MoveMessagesInteractorImpl(executor: Executor) :
        BaseInteractor(executor),
        MoveMessagesInteractor {
    override var output: MoveMessagesInteractor.Output? = null
    override var input: MoveMessagesInteractor.Input? = null


    override fun execute() {
        // Todo implement move route?


        Thread.sleep(1000)

        runOnUIThread {
            output?.onMoveMessagesSuccess()
        }

    }
}