package dk.eboks.app.domain.interactors.user

import dk.eboks.app.domain.interactors.authentication.mobileacces.DeleteRSAKeyForUserInteractor
import dk.eboks.app.domain.managers.UserManager
import dk.eboks.app.domain.managers.UserSettingsManager
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.login.UserSettings
import dk.eboks.app.util.exceptionToViewError
import dk.eboks.app.util.guard
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor
import timber.log.Timber

/**
 * Created by bison on 24-06-2017.
 */
class DeleteUserInteractorImpl(
        executor: Executor,
        val userManager: UserManager,
        val userSettingsManager: UserSettingsManager,
        val deleteRSAKeyForUserInteractor: DeleteRSAKeyForUserInteractor
) : BaseInteractor(executor), DeleteUserInteractor {
    override var output: DeleteUserInteractor.Output? = null
    override var input: DeleteUserInteractor.Input? = null

    override fun execute() {
        // we don't use input in this example (chnt: Yes we do!):
        try {
            input?.user?.let { user ->
                userManager.remove(user)
                deleteRSAKeyForUserInteractor.input =
                    DeleteRSAKeyForUserInteractor.Input(user.id.toString())
                deleteRSAKeyForUserInteractor.run()
                userSettingsManager.remove(UserSettings(user.id)) // also remove the settings for that userId
                runOnUIThread {
                    output?.onDeleteUser(user)
                }
            }.guard {
                runOnUIThread {
                    output?.onDeleteUserError(
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
                output?.onDeleteUserError(exceptionToViewError(t))
            }
        }
    }
}