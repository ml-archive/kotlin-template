package dk.eboks.app.domain.interactors.message

import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.repositories.MessagesRepository
import dk.eboks.app.util.exceptionToViewError
import dk.eboks.app.util.guard
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor
import timber.log.Timber

class SubmitReplyFormInteractorImpl(executor: Executor, val messagesRepository: MessagesRepository) : BaseInteractor(executor), SubmitReplyFormInteractor {
    override var output: SubmitReplyFormInteractor.Output? = null
    override var input: SubmitReplyFormInteractor.Input? = null

    override fun execute() {
        try {
            input?.let { args->
                messagesRepository.submitMessageReplyForm(args.msg, args.form)
                runOnUIThread {
                    output?.onSubmitReplyForm()
                }
            }.guard {
                throw(RuntimeException("bad interactor input"))
            }
        }
        catch (t : Throwable)
        {
            Timber.e(t)
            runOnUIThread {
                output?.onSubmitReplyFormError(exceptionToViewError(t, shouldClose = true))
            }
        }
    }

}