package dk.eboks.app.domain.interactors.user

import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.managers.UserManager
import dk.eboks.app.domain.managers.UserSettingsManager
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.network.Api
import dk.eboks.app.util.exceptionToViewError
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor

/**
 * Created by Christian on 5/7/2018.
 * @author Christian
 * @since 5/7/2018.
 */
class GetUserProfileInteractorImpl(
        executor: Executor,
        val api: Api,
        val appStateManager: AppStateManager,
        val userManager: UserManager,
        val userSettingsManager: UserSettingsManager
) : BaseInteractor(executor), GetUserProfileInteractor {

    override var output: GetUserProfileInteractor.Output? = null

    override fun execute() {
        try {
            val result = api.getUserProfile().execute()
            result?.body()?.let {
                userManager.put(it)
                appStateManager.state?.currentUser = it
                appStateManager.state?.currentSettings =
                    userSettingsManager.get(it.id) // also load settings
                appStateManager.save()

                runOnUIThread {
                    output?.onGetUser(it)
                }
                return
            }
        } catch (t: Throwable) {
            runOnUIThread {
                output?.onGetUserError(exceptionToViewError(t))
            }
            return
        }
        runOnUIThread {
            output?.onGetUserError(
                ViewError(
                    title = Translation.error.startupTitle,
                    message = Translation.error.startupMessage,
                    shouldCloseView = false
                )
            )
        }
        return
    }
}