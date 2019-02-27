package dk.eboks.app.domain.interactors.user

import dk.eboks.app.domain.repositories.UserRepository
import dk.eboks.app.util.exceptionToViewError
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor
import javax.inject.Inject

class CheckSsnExistsInteractorImpl @Inject constructor(
    executor: Executor,
    private val userRestRepo: UserRepository
) : BaseInteractor(executor), CheckSsnExistsInteractor {
    override var output: CheckSsnExistsInteractor.Output? = null
    override var input: CheckSsnExistsInteractor.Input? = null

    override fun execute() {
        var exists = true
        try {
            input?.ssn?.let {
                exists = userRestRepo.checkSsn(it)
            }
            runOnUIThread {
                output?.onCheckSsnExists(exists)
            }
        } catch (t: Throwable) {
            runOnUIThread {
                output?.onCheckSsnExists(exceptionToViewError(t, shouldDisplay = false))
            }
        }
    }
}