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
 * Created by bison on 24-06-2017.
 */
class LoginInteractorImpl(executor: Executor, val api: Api, val appStateManager: AppStateManager, val userManager: UserManager, val authClient: AuthClient, val cacheManager: CacheManager) : BaseInteractor(executor), LoginInteractor {
    override var output: LoginInteractor.Output? = null
    override var input: LoginInteractor.Input? = null

    override fun execute() {
        try {
            input?.let { args ->
                try {
                    val token = authClient.login(username = args.loginState.userName
                            ?: "", password = args.loginState.userPassWord
                            ?: "", activationCode = args.loginState.activationCode)

                    token?.let { token ->
                        appStateManager.state?.loginState?.token = token

                        val userResult = api.getUserProfile().execute()
                        userResult?.body()?.let { user ->
                            // update the states
                            appStateManager.state?.loginState?.userLoginProviderId?.let { user.lastLoginProviderId = it }
                            args.loginState.activationCode?.let {
                                user.activationCode = it
                            }
                            Timber.e("Saving user $user")
                            val newUser = userManager.put(user)

                            appStateManager.state?.loginState?.lastUser?.let { lastUser ->
                                if (lastUser.id != newUser.id) {
                                    Timber.e("Different user id detected on login, clearing caches")
                                    cacheManager.clearStores()
                                }
                            }

                            appStateManager.state?.loginState?.lastUser = newUser
                            appStateManager.state?.currentUser = newUser
                        }
                        appStateManager.save()

                        runOnUIThread {
                            output?.onLoginSuccess(token)
                        }
                    }.guard {
                        runOnUIThread {
                            output?.onLoginError(ViewError(title = Translation.error.genericTitle, message = Translation.error.genericMessage, shouldCloseView = true)) // TODO better error
                        }
                    }
                }
                catch (e : AuthException)
                {
                    e.printStackTrace()
                    if(e.httpCode == 400)
                    {
                        runOnUIThread {
                            output?.onLoginActivationCodeRequired()
                        }
                    }
                    else
                    {
                        runOnUIThread {
                            output?.onLoginDenied(ViewError(title = Translation.error.genericTitle, message = Translation.logoncredentials.invalidPassword, shouldCloseView = true)) // TODO better error
                        }
                    }
                }

            }
        } catch (t: Throwable) {
            runOnUIThread {
                output?.onLoginError(exceptionToViewError(t))
            }
        }
    }
}