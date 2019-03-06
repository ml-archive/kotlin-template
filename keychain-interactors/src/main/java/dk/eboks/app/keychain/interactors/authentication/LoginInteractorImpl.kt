package dk.eboks.app.keychain.interactors.authentication

import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.managers.AuthClient
import dk.eboks.app.domain.managers.AuthException
import dk.eboks.app.domain.managers.CacheManager
import dk.eboks.app.domain.managers.UserManager
import dk.eboks.app.domain.managers.UserSettingsManager
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
import javax.inject.Inject

/**
 * Created by bison on 24-06-2017.
 */
internal class LoginInteractorImpl @Inject constructor(
    executor: Executor,
    private val api: Api,
    private val appStateManager: AppStateManager,
    private val userManager: UserManager,
    private val userSettingsManager: UserSettingsManager,
    private val authClient: AuthClient,
    private val cacheManager: CacheManager,
    private val foldersRepositoryMail: MailCategoriesRepository
) : BaseInteractor(executor), LoginInteractor {
    override var output: LoginInteractor.Output? = null
    override var input: LoginInteractor.Input? = null

    override fun execute() {
        try {
            input?.let { args ->
                try {
                    val useLongToken =
                        userSettingsManager[args.loginState.selectedUser?.id ?: -1].stayLoggedIn

                    var userid = "no id"
                    args.loginState.lastUser?.id?.let {
                        userid = it.toString()
                    }
                    args.loginState.selectedUser?.id?.let {
                        userid = it.toString()
                    }

                    val token = authClient.login(
                        username = args.loginState.userName ?: "",
                        password = args.loginState.userPassWord ?: "",
                        longClient = useLongToken,
                        bearerToken = args.bearerToken,
                        userid = userid
                    )

                    token?.let { t ->
                        appStateManager.state?.loginState?.token = t

                        val userResult = api.getUserProfile().execute()
                        userResult.body()?.let { user ->
                            // update the states
                            Timber.e("Saving currentUser $user on login")
                            val newUser = userManager.put(user)
                            val newSettings = userSettingsManager[newUser.id]

                            appStateManager.state?.loginState?.userLoginProviderId?.let {
                                newSettings.lastLoginProviderId = it
                            }
                            args.loginState.activationCode?.let {
                                newSettings.activationCode = it
                            }

                            /*
                            appStateManager.state?.loginState?.lastUser?.let { lastUser ->
                                if (lastUser.id != newUser.id) {
                                    Timber.e("Different currentUser id detected on login, clearing caches")
                                    cacheManager.clearStores()
                                }
                            }
                            */
                            cacheManager.clearStores()

                            userSettingsManager.put(newSettings)
                            appStateManager.state?.openingState?.acceptPrivateTerms = false
                            appStateManager.state?.loginState?.lastUser = newUser
                            appStateManager.state?.currentUser = newUser
                            appStateManager.state?.currentSettings = newSettings
                        }
//                        appStateManager.state?.loginState?.userName = ""
//                        appStateManager.state?.loginState?.userPassWord = ""

                        // clear message opening state
                        appStateManager.state?.openingState?.acceptPrivateTerms = false
                        appStateManager.state?.openingState?.sendReceipt = false
                        appStateManager.state?.openingState?.shouldProceedWithOpening = false
                        appStateManager.state?.openingState?.serverError = null

                        appStateManager.save()

                        EAuth2.ignoreFurtherLoginRequests = false

                        runOnUIThread {
                            output?.onLoginSuccess(t)
                        }

                        appStateManager.state?.selectedFolders =
                            foldersRepositoryMail.getMailCategories(
                                false,
                                appStateManager.state?.impersoniateUser?.userId
                            )
                    }.guard {
                        runOnUIThread {
                            output?.onLoginError(
                                ViewError(
                                    title = Translation.error.genericTitle,
                                    message = Translation.error.genericMessage,
                                    shouldCloseView = true
                                )
                            ) // TODO better error
                        }
                    }
                } catch (e: AuthException) {
                    Timber.e("AuthException = ${e.httpCode} ${e.errorDescription}")
                    if (e.httpCode == 400) {
                        runOnUIThread {
                            when {
                                e.errorDescription.contentEquals("User verification error") -> output?.onLoginDenied(
                                    ViewError(
                                        title = Translation.logoncredentials.invalidCredentialsTitle,
                                        message = Translation.logoncredentials.invalidCredentialsMessage,
                                        shouldCloseView = false
                                    )
                                )
                                e.errorDescription.contentEquals("Activation code is required") -> output?.onLoginActivationCodeRequired()
                                else -> output?.onLoginDenied(
                                    ViewError(
                                        title = Translation.error.genericTitle,
                                        message = Translation.error.genericMessage,
                                        shouldCloseView = false
                                    )
                                )
                            }
                        }
                    } else {
                        runOnUIThread {
                            output?.onLoginDenied(
                                ViewError(
                                    title = Translation.error.genericTitle,
                                    message = Translation.error.genericMessage,
                                    shouldCloseView = true
                                )
                            )
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