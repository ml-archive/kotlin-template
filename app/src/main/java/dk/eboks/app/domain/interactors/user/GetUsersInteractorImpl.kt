package dk.eboks.app.domain.interactors.user

import dk.eboks.app.domain.managers.UserManager
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor

/**
 * Created by bison on 24-06-2017.
 */
class GetUsersInteractorImpl(executor: Executor, val userManager: UserManager) : BaseInteractor(executor), GetUsersInteractor {
    override var output : GetUsersInteractor.Output? = null

    override fun execute() {
        try {
            runOnUIThread {
                output?.onGetUsers(userManager.users)
            }
        } catch (e: Exception) {
            runOnUIThread {
                output?.onGetUsersError(e.message ?: "Unknown error")
            }
        }
    }
}