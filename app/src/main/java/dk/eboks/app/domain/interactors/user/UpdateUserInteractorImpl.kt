package dk.eboks.app.domain.interactors.user

import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor
import dk.eboks.app.network.Api
import dk.eboks.app.network.repositories.UserRestRepository
import dk.eboks.app.util.exceptionToViewError

class UpdateUserInteractorImpl(executor: Executor, val api: Api, val userRestRepo: UserRestRepository) : BaseInteractor(executor), UpdateUserInteractor {
    override var output: UpdateUserInteractor.Output? = null
    override var input: UpdateUserInteractor.Input? = null


    override fun execute() {
        try {

            input?.user?.let {
                userRestRepo.updateProfile(it)
            }
            runOnUIThread {
                output?.onUpdateProfile()
            }
        } catch (t: Throwable) {
            runOnUIThread {
                output?.onUpdateProfileError(exceptionToViewError(t, shouldDisplay = false))
            }
        }

    }
}