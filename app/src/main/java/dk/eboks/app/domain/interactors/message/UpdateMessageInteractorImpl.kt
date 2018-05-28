package dk.eboks.app.domain.interactors.message

import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.repositories.MessagesRepository
import dk.eboks.app.util.exceptionToViewError
import dk.eboks.app.util.guard
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor
import timber.log.Timber

class UpdateMessageInteractorImpl(executor: Executor, val messagesRepository: MessagesRepository) : BaseInteractor(executor),
                                                        UpdateMessageInteractor {
    override var input: UpdateMessageInteractor.Input? = null
    override var output: UpdateMessageInteractor.Output? = null


    override fun execute() {
        try {
            if (input?.message != null && input?.messagePatch != null) {
                messagesRepository.updateMessage(input!!.message, input!!.messagePatch)
                runOnUIThread { output?.onUpdateMessageSuccess() }
            } else {
                runOnUIThread { output?.onUpdateMessageError(ViewError()) }
            }
        }
        catch (t: Throwable) {
            runOnUIThread {
                Timber.e(t)
                output?.onUpdateMessageError(exceptionToViewError(t))
            }
        }
    }
}