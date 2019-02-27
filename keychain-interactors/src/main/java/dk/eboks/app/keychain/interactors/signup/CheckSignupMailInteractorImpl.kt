package dk.eboks.app.keychain.interactors.signup

import dk.eboks.app.domain.repositories.SignupRepository
import dk.eboks.app.util.exceptionToViewError
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor
import javax.inject.Inject

class CheckSignupMailInteractorImpl @Inject constructor(
    executor: Executor,
    private val signUpRestRepo: SignupRepository
) : BaseInteractor(executor), CheckSignupMailInteractor {
    override var output: CheckSignupMailInteractor.Output? = null
    override var input: CheckSignupMailInteractor.Input? = null

    override fun execute() {
        var exists = true
        try {
            input?.email?.let {
                exists = signUpRestRepo.verifySignupMail(it)
            }
            runOnUIThread {
                output?.onVerifySignupMail(exists)
            }
        } catch (t: Throwable) {
            runOnUIThread {
                output?.onVerifySignupMail(exceptionToViewError(t, shouldDisplay = false))
            }
        }
    }
}