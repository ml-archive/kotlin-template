package dk.eboks.app.domain.interactors.user

import dk.eboks.app.domain.managers.UserManager
import dk.eboks.app.util.guard
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor

/**
 * Created by bison on 24-06-2017.
 */
class SaveUserInteractorImpl(executor: Executor, val userManager: UserManager) : BaseInteractor(executor), SaveUserInteractor {
    override var output : SaveUserInteractor.Output? = null
    override var input : SaveUserInteractor.Input? = null

    override fun execute() {
        // we don't use input in this example but we could:
        try {
            input?.user?.let { user->
                userManager.save(user)
                runOnUIThread {
                    output?.onSaveUser(user, userManager.users.size)
                }
            }.guard {
                runOnUIThread {
                    output?.onSaveUserError("Interactor missing input")
                }
            }

        } catch (e: Exception) {
            runOnUIThread {
                output?.onSaveUserError(e.message ?: "Unknown error")
            }
        }
    }
}