package dk.eboks.app.profile.interactors

import dk.eboks.app.domain.repositories.UserRepository
import dk.eboks.app.util.exceptionToViewError
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor
import javax.inject.Inject

internal class VerifyPhoneInteractorImpl @Inject constructor(
    executor: Executor,
    private val userRestRepo: UserRepository
) : BaseInteractor(executor), VerifyPhoneInteractor {
    override var output: VerifyPhoneInteractor.Output? = null
    override var input: VerifyPhoneInteractor.Input? = null

    override fun execute() {
        try {
            input?.number?.let {
                userRestRepo.verifyPhone(it)
            }
            runOnUIThread {
                output?.onVerifyPhone()
            }
        } catch (t: Throwable) {
            t.printStackTrace()
            runOnUIThread {
                output?.onVerifyPhoneError(exceptionToViewError(t))
            }
        }
    }
}