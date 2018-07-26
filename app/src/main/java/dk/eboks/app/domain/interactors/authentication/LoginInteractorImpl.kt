package dk.eboks.app.domain.interactors.authentication

import dk.eboks.app.domain.managers.*
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.repositories.MailCategoriesRepository
import dk.eboks.app.network.Api
import dk.eboks.app.network.managers.protocol.EAuth2
import dk.eboks.app.util.exceptionToViewError
import dk.eboks.app.util.guard
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor
import timber.log.Timber

/**
 * Created by bison on 24-06-2017.
 */
class LoginInteractorImpl(
        executor: Executor, val api: Api,
        val appStateManager: AppStateManager,
        val userManager: UserManager,
        val userSettingsManager: UserSettingsManager,
        val authClient: AuthClient,
        val cacheManager: CacheManager,
        val foldersRepositoryMail: MailCategoriesRepository) : BaseInteractor(executor), LoginInteractor {
    override var output: LoginInteractor.Output? = null
    override var input: LoginInteractor.Input? = null

    override fun execute() {
        try {
            input?.let { args ->
                try {
                    val useLongToken = userSettingsManager.get(args.loginState.selectedUser?.id ?: -1).stayLoggedIn

                    val token = authClient.login(
                            username = args.loginState.userName ?: "",
                            password = args.loginState.userPassWord ?: "",
                            activationCode = args.loginState.activationCode,
                            longClient = useLongToken,
                            bearerToken = args.bearerToken
                    )

                    token?.let { t ->
                        appStateManager.state?.loginState?.token = t

                        val userResult = api.getUserProfile().execute()
                        userResult?.body()?.let { user ->
                            // update the states
                            Timber.i("Saving user $user")
                            val newUser = userManager.put(user)
                            val newSettings = userSettingsManager.get(newUser.id)

                            appStateManager.state?.loginState?.userLoginProviderId?.let {
                                newSettings.lastLoginProviderId = it
                            }
                            args.loginState.activationCode?.let {
                                newSettings.activationCode = it
                            }

                            /*
                            appStateManager.state?.loginState?.lastUser?.let { lastUser ->
                                if (lastUser.id != newUser.id) {
                                    Timber.e("Different user id detected on login, clearing caches")
                                    cacheManager.clearStores()
                                }
                            }
                            */
                            cacheManager.clearStores()

                            userSettingsManager.put(newSettings)
                            appStateManager.state?.loginState?.lastUser = newUser
                            appStateManager.state?.currentUser = newUser
                            appStateManager.state?.currentSettings = newSettings
                        }
                        appStateManager.state?.loginState?.userName = ""
                        appStateManager.state?.loginState?.userPassWord = ""
                        appStateManager.save()

                        EAuth2.ignoreFurtherLoginRequests = false

                        runOnUIThread {
                            output?.onLoginSuccess(t)
                        }

                        appStateManager.state?.selectedFolders = foldersRepositoryMail.getMailCategories(false)

                    }.guard {
                        runOnUIThread {
                            output?.onLoginError(ViewError(title = Translation.error.genericTitle, message = Translation.error.genericMessage, shouldCloseView = true)) // TODO better error
                        }
                    }
                }
                catch (e : AuthException)
                {
                    Timber.e("AuthException = ${e.httpCode} ${e.errorDescription}")
                    if(e.httpCode == 400)
                    {
                        runOnUIThread {
                            when {
                                e.errorDescription.contentEquals("User verification error") -> output?.onLoginDenied(ViewError(
                                        title = Translation.logoncredentials.invalidCredentialsTitle,
                                        message = Translation.logoncredentials.invalidCredentialsMessage,
                                        shouldCloseView = false))
                                e.errorDescription.contentEquals("Activation code is required") -> output?.onLoginActivationCodeRequired()
                                else -> output?.onLoginDenied(ViewError(title = Translation.error.genericTitle, message = Translation.error.genericMessage, shouldCloseView = false))
                            }
                        }
                    }
                    else
                    {
                        runOnUIThread {
                            output?.onLoginDenied(ViewError(title = Translation.error.genericTitle, message = Translation.error.genericMessage, shouldCloseView = true))
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