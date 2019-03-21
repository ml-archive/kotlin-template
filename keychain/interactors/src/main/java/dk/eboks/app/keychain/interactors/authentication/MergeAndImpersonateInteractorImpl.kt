package dk.eboks.app.keychain.interactors.authentication

import dk.eboks.app.domain.exceptions.InteractorException
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.managers.AuthClient
import dk.eboks.app.domain.managers.CacheManager
import dk.eboks.app.domain.managers.UserManager
import dk.eboks.app.domain.managers.UserSettingsManager
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.login.UserSettings
import dk.eboks.app.domain.repositories.MailCategoriesRepository
import dk.eboks.app.network.Api
import dk.eboks.app.util.exceptionToViewError
import dk.eboks.app.util.guard
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Christian on 5/28/2018.
 * @author Christian
 * @since 5/28/2018.
 */
internal class MergeAndImpersonateInteractorImpl @Inject constructor(
    executor: Executor,
    private val api: Api,
    private val appStateManager: AppStateManager,
    private val userManager: UserManager,
    private val userSettingsManager: UserSettingsManager,
    private val authClient: AuthClient,
    private val cacheManager: CacheManager,
    private val foldersRepositoryMail: MailCategoriesRepository
) : BaseInteractor(executor), MergeAndImpersonateInteractor {
    override var output: MergeAndImpersonateInteractor.Output? = null
    override var input: MergeAndImpersonateInteractor.Input? = null

    override fun execute() {
        try {
            input?.verificationState?.let { verificationState ->
                var targetUserId: String
                // if user choose to merge profiles, do that first, otherwise impersonate only
                if (verificationState.shouldMergeProfiles) {
                    verificationState.allowMigrateUserId?.let { userId ->
                        targetUserId = userId
                        val result = api.migrateUser(userId).execute()
                        if (!result.isSuccessful) {
                            runOnUIThread {
                                output?.onMergeError(
                                    ViewError(
                                        title = Translation.error.genericTitle,
                                        message = Translation.error.genericMessage,
                                        shouldCloseView = true
                                    )
                                ) // TODO better error
                            }
                            return
                        }
                        verificationState.userBeingVerified?.let { olduser ->
                            Timber.e("Removing old user from manager after successful migration")
                            userManager.remove(olduser)
                            userSettingsManager.remove(UserSettings(olduser.id)) // also remove the settings for that userId
                        }
                        // after migrate call impersonate
                        appStateManager.state?.loginState?.token?.let {
                            val newToken = authClient.impersonate(it.access_token, targetUserId)
                            newToken?.let {
                                appStateManager.state?.loginState?.token = it
                            }
                        }
                    }
                }

                appStateManager.state?.loginState?.token?.let {
                    val userResult = api.getUserProfile().execute()
                    userResult.body()?.let { user ->
                        // update the states
                        Timber.e("Saving user $user")
                        val newUser = userManager.put(user)
                        val newSettings = userSettingsManager[newUser.id]

                        appStateManager.state?.loginState?.userLoginProviderId?.let {
                            newSettings.lastLoginProviderId = it
                        }
                        appStateManager.state?.loginState?.activationCode?.let {
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
                        appStateManager.state?.verificationState = null
                    }
                }.guard { throw(InteractorException("no token found to impersonate")) }

                appStateManager.save()
                runOnUIThread {
                    output?.onMergeCompleted()
                }
                appStateManager.state?.selectedFolders = foldersRepositoryMail.getMailCategories(
                    false,
                    appStateManager.state?.impersoniateUser?.userId
                )
            }.guard {
                runOnUIThread {
                    output?.onMergeError(
                        ViewError(
                            title = Translation.error.genericTitle,
                            message = Translation.error.genericMessage,
                            shouldCloseView = true
                        )
                    ) // TODO better error
                }
            }
        } catch (t: Throwable) {
            runOnUIThread {
                output?.onMergeError(exceptionToViewError(t))
            }
        }
    }
}