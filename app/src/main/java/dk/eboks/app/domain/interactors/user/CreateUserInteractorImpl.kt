package dk.eboks.app.domain.interactors.user

import dk.eboks.app.domain.managers.UserManager
import dk.eboks.app.util.guard
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor

/**
 * Created by bison on 24-06-2017.
 */
class CreateUserInteractorImpl(executor: Executor, val userManager: UserManager) : BaseInteractor(executor), CreateUserInteractor {
    override var output : CreateUserInteractor.Output? = null
    override var input : CreateUserInteractor.Input? = null

    override fun execute() {
        // we don't use input in this example but we could:
        try {
            input?.user?.let { user->
                userManager.add(user)
                runOnUIThread {
                    output?.onCreateUser(user)
                }
            }.guard {
                runOnUIThread {
                    output?.onCreateUserError("Interactor missing input")
                }
            }

        } catch (e: Exception) {
            runOnUIThread {
                output?.onCreateUserError(e.message ?: "Unknown error")
            }
        }
    }
}