package dk.eboks.app.keychain.interactors.authentication

import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.managers.UserManager
import dk.eboks.app.domain.managers.UserSettingsManager
import dk.eboks.app.network.Api
import dk.eboks.app.util.exceptionToViewError
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Christian on 5/28/2018.
 * @author Christinus
 * @since 5/28/2018.
 */
internal class SetCurrentUserInteractorImpl @Inject constructor(
    executor: Executor,
    private val api: Api,
    private val appStateManager: AppStateManager,
    private val userManager: UserManager,
    private val userSettingsManager: UserSettingsManager
) : BaseInteractor(executor), SetCurrentUserInteractor {
    override var output: SetCurrentUserInteractor.Output? = null

    override fun execute() {
        try {
            val userResult = api.getUserProfile().execute()
            userResult.body()?.let { user ->
                // update the states
                Timber.e("Setting current user $user")
                val newUser = userManager.put(user)
                val newSettings = userSettingsManager.get(newUser.id)
                userSettingsManager.put(newSettings)
                appStateManager.state?.loginState?.lastUser = newUser
                appStateManager.state?.currentUser = newUser
                appStateManager.state?.currentSettings = newSettings
            }
            appStateManager.save()
            runOnUIThread {
                output?.onSetCurrentUserSuccess()
            }
        } catch (t: Throwable) {
            runOnUIThread {
                output?.onSetCurrentUserError(exceptionToViewError(t))
            }
        }
    }
}