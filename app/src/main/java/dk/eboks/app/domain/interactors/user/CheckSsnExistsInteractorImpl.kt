package dk.eboks.app.domain.interactors.user

import dk.eboks.app.domain.repositories.UserRepository
import dk.eboks.app.network.Api
import dk.eboks.app.util.exceptionToViewError
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor

class CheckSsnExistsInteractorImpl(
    executor: Executor,
    val api: Api,
    val userRestRepo: UserRepository
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