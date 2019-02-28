package dk.eboks.app.profile.interactors

import dk.eboks.app.domain.managers.UserManager
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.util.exceptionToViewError
import dk.eboks.app.util.guard
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor
import javax.inject.Inject

/**
 * Created by bison on 24-06-2017.
 */
class SaveUserInteractorImpl @Inject constructor(
    executor: Executor,
    private val userManager: UserManager
) :
    BaseInteractor(executor), SaveUserInteractor {
    override var output: SaveUserInteractor.Output? = null
    override var input: SaveUserInteractor.Input? = null

    override fun execute() {
        try {
            input?.user?.let { user ->
                userManager.put(user)
                runOnUIThread {
                    output?.onSaveUser(user, userManager.users.size)
                }
            }.guard {
                runOnUIThread {
                    output?.onSaveUserError(ViewError())
                }
            }
        } catch (t: Throwable) {
            runOnUIThread {
                output?.onSaveUserError(exceptionToViewError(t))
            }
        }
    }
}