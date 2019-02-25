package dk.eboks.app.domain.interactors.user

import dk.eboks.app.domain.managers.UserManager
import dk.eboks.app.util.exceptionToViewError
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor
import javax.inject.Inject

/**
 * Created by bison on 24-06-2017.
 */
class SaveUsersInteractorImpl @Inject constructor(
    executor: Executor,
    private val userManager: UserManager
) :
    BaseInteractor(executor), SaveUsersInteractor {
    override var output: SaveUsersInteractor.Output? = null

    override fun execute() {
        // we don't use input in this example but we could:
        try {
            userManager.save()
            runOnUIThread {
                output?.onSaveUsers()
            }
        } catch (t: Throwable) {
            runOnUIThread {
                output?.onSaveUsersError(exceptionToViewError(t))
            }
        }
    }
}