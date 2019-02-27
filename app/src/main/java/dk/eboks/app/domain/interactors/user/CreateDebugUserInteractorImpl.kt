package dk.eboks.app.domain.interactors.user

import dk.eboks.app.domain.managers.UserManager
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.keychain.interactors.user.CreateUserInteractor
import dk.eboks.app.util.exceptionToViewError
import dk.eboks.app.util.guard
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Christian on 5/29/2018.
 * @author Christian
 * @since 5/29/2018.
 */
class CreateDebugUserInteractorImpl @Inject constructor(
    executor: Executor,
    private val userManager: UserManager
) : BaseInteractor(executor), CreateUserInteractor {
    override var output: CreateUserInteractor.Output? = null
    override var input: CreateUserInteractor.Input? = null

    override fun execute() {
        // we don't use input in this example but we could:
        try {
            input?.user?.let { user ->
                userManager.put(user)
                runOnUIThread {
                    output?.onCreateUser(user)
                }
            }.guard {
                runOnUIThread {
                    output?.onCreateUserError(
                        ViewError(
                            Translation.error.genericStorageTitle,
                            Translation.error.genericStorageMessage
                        )
                    )
                }
            }
        } catch (t: Throwable) {
            Timber.e(t)
            runOnUIThread {
                output?.onCreateUserError(exceptionToViewError(t))
            }
        }
    }
}