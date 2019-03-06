package dk.eboks.app.keychain.interactors.signup

import dk.eboks.app.domain.exceptions.InteractorException
import dk.eboks.app.domain.repositories.SignupRepository
import dk.eboks.app.util.exceptionToViewError
import dk.eboks.app.util.guard
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor
import javax.inject.Inject

internal class CheckSignupMailInteractorImpl @Inject constructor(
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
            }.guard { throw(InteractorException("bad args")) }
            runOnUIThread {
                output?.onVerifySignupMail(exists)
            }
        } catch (t: Throwable) {
            runOnUIThread {
                output?.onVerifySignupMailError(exceptionToViewError(t, shouldDisplay = false))
            }
        }
    }
}