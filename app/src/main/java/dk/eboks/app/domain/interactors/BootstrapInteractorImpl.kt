package dk.eboks.app.domain.interactors

import dk.eboks.app.domain.config.AppConfig
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.managers.CacheManager
import dk.eboks.app.domain.managers.FileCacheManager
import dk.eboks.app.domain.managers.GuidManager
import dk.eboks.app.domain.managers.PrefManager
import dk.eboks.app.domain.managers.UserManager
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.repositories.SettingsRepository
import dk.eboks.app.network.Api
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor
import timber.log.Timber
import javax.inject.Inject
import javax.net.ssl.SSLPeerUnverifiedException

/**
 * Created by bison on 24-06-2017.
 */
class BootstrapInteractorImpl @Inject constructor(
    executor: Executor,
    private val guidManager: GuidManager,
    private val settingsRepository: SettingsRepository,
    private val appStateManager: AppStateManager,
    private val fileCacheManager: FileCacheManager,
    private val cacheManager: CacheManager,
    private val userManager: UserManager,
    private val api: Api,
    private val prefManager: PrefManager,
    private val appConfig: AppConfig
) : BaseInteractor(executor), BootstrapInteractor {
    override var output: BootstrapInteractor.Output? = null
    override var input: BootstrapInteractor.Input? = null

    override fun execute() {

        try {
            // we don't use input in this example but we could:
            input?.let {
                // do something with unwrapped input
            }
            if (appConfig.isDebug) {
                // do we have a saved config?
                prefManager.getString("config", null)?.let {
                    Timber.e("Setting config to $it")
                    appConfig.changeConfig(it)
                }
                // do we have a saved environment? then set it
                prefManager.getString("environment", null)?.let {
                    Timber.e("Setting environment to $it")
                    appConfig.changeEnvironment(it)
                }
            }

            val result = api.getResourceLinks().execute()
            if (result.isSuccessful) {
                result.body()?.let { links ->
                    appConfig.resourceLinks = links
                }
            }

            val settings = settingsRepository.get()
            if (settings.deviceId.isBlank()) {
                settings.deviceId = guidManager.generateGuid()
                Timber.d("No device ID found, generating new id: ${settings.deviceId}")
            }
            settingsRepository.put(settings)

            val hasUsers = userManager.users.isNotEmpty()
            val stayLoggedIn = true == appStateManager.state?.currentSettings?.stayLoggedIn
            val loginState = appStateManager.state?.loginState
            loginState?.firstLogin = !hasUsers

            if (!stayLoggedIn) {
                loginState?.kspToken = ""
                loginState?.token = null
                loginState?.activationCode = null
                loginState?.userName = null
                loginState?.userPassWord = null
                loginState?.selectedUser = null
                appStateManager.state?.currentUser = null
                appStateManager.state?.currentFolder = null
                appStateManager.state?.currentMessage = null
                appStateManager.state?.currentSettings = null
                appStateManager.state?.currentViewerFileName = null
                appStateManager.state?.verificationState = null

                // clear memory caches, this is necessary when the app hasn't been force closed in case
                // another user is logged in
                Timber.d("Clearing CacheStore memory")
                cacheManager.clearStoresMemoryOnly()
                fileCacheManager.clearMemoryOnly()
            }

            Timber.d("LoginState: $loginState?")
            // Thread.sleep(2000)
            runOnUIThread {
                output?.onBootstrapDone(hasUsers, stayLoggedIn)
            }
        } catch (e: Exception) {
            Timber.e(e)
            runOnUIThread {
                if (e is SSLPeerUnverifiedException) {
                    output?.onBootstrapError(
                        ViewError(
                            title = Translation.error.compromisedConnectionTitle,
                            message = Translation.error.compromisedConnectionMessage,
                            shouldCloseView = true
                        )
                    )
                } else {
                    output?.onBootstrapError(
                        ViewError(
                            title = Translation.error.startupTitle,
                            message = Translation.error.startupMessage,
                            shouldCloseView = true
                        )
                    )
                }
            }
        }
    }
}