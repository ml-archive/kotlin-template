package dk.eboks.app.domain.interactors.signup

import dk.eboks.app.network.Api
import dk.eboks.app.network.repositories.SignupRestRepository
import dk.eboks.app.util.exceptionToViewError
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor

class CheckSignupMailInteractorImpl(
    executor: Executor,
    val api: Api,
    val signUpRestRepo: SignupRestRepository
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