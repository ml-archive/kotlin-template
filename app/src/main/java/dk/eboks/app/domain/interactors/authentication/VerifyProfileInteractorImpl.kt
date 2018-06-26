package dk.eboks.app.domain.interactors.authentication

import dk.eboks.app.domain.managers.*
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.network.Api
import dk.eboks.app.util.exceptionToViewError
import dk.eboks.app.util.guard
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor
import timber.log.Timber


/**
 * Created by Christian on 5/28/2018.
 * @author   Christian
 * @since    5/28/2018.
 */
class VerifyProfileInteractorImpl(
        executor: Executor, val api: Api,
        val appStateManager: AppStateManager,
        val userManager: UserManager,
        val userSettingsManager: UserSettingsManager,
        val authClient: AuthClient,
        val cacheManager: CacheManager
) : BaseInteractor(executor), VerifyProfileInteractor {
    override var output: VerifyProfileInteractor.Output? = null
    override var input: VerifyProfileInteractor.Input? = null

    override fun execute() {
        try {
            input?.verificationState?.let { verificationState ->
                authClient.transformKspToken(verificationState.kspToken)?.let { token ->
                    appStateManager.state?.loginState?.token = token

                    authClient.decodeJWT(verificationState.kspToken)
                    /*
                    val userResult = api.getUserProfile().execute()
                    userResult?.body()?.let { user->
                        // update the states
                        Timber.e("Saving user $user")
                        val newUser = userManager.put(user)
                        val newSettings = userSettingsManager.get(newUser.id)

                        appStateManager.state?.loginState?.userLoginProviderId?.let {
                            newSettings.lastLoginProviderId = it
                        }
                        appStateManager.state?.loginState?.activationCode?.let {
                            newSettings.activationCode = it
                        }

                        appStateManager.state?.loginState?.lastUser?.let { lastUser ->
                            if (lastUser.id != newUser.id) {
                                Timber.e("Different user id detected on login, clearing caches")
                                cacheManager.clearStores()
                            }
                        }

                        userSettingsManager.put(newSettings)
                        appStateManager.state?.loginState?.lastUser = newUser
                        appStateManager.state?.currentUser = newUser
                        appStateManager.state?.currentSettings = newSettings
                    }
                    appStateManager.save()
                    */

                    runOnUIThread {
                        //output?.onLoginSuccess(token)
                    }
                }.guard {
                    runOnUIThread {
                        //output?.onLoginError(ViewError(title = Translation.error.genericTitle, message = Translation.error.genericMessage, shouldCloseView = true)) // TODO better error
                    }
                }
            }
        } catch (t: Throwable) {
            runOnUIThread {
                output?.onError(exceptionToViewError(t))
            }
        }
    }

}