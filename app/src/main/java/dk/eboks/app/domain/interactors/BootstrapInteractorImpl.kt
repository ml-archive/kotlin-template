package dk.eboks.app.domain.interactors

import dk.eboks.app.domain.managers.*
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.login.LoginState
import dk.eboks.app.domain.repositories.SettingsRepository
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor
import timber.log.Timber

/**
 * Created by bison on 24-06-2017.
 */
class BootstrapInteractorImpl(executor: Executor, val guidManager: GuidManager, val settingsRepository: SettingsRepository,
                              val appStateManager: AppStateManager,
                              val fileCacheManager: FileCacheManager, val userManager: UserManager) : BaseInteractor(executor), BootstrapInteractor {
    override var output: BootstrapInteractor.Output? = null
    override var input: BootstrapInteractor.Input? = null

    override fun execute() {

        try {
            // we don't use input in this example but we could:
            input?.let {
                // do something with unwrapped input

            }
            val hasUsers = userManager.users.isNotEmpty()

            val loginState = appStateManager.state?.loginState
            loginState?.firstLogin = !hasUsers

            val settings = settingsRepository.get()
            if (settings.deviceId.isBlank()) {
                settings.deviceId = guidManager.generateGuid()
                Timber.d("No device ID found, generating new id: ${settings.deviceId}")
            }
            settingsRepository.put(settings)

            loginState?.kspToken = ""
            loginState?.token = null
            loginState?.activationCode = null
            loginState?.userName = null
            loginState?.userPassWord = null


            Timber.d("LoginState: $loginState?")
            //Thread.sleep(2000)
            runOnUIThread {
                output?.onBootstrapDone(hasUsers)
            }
        } catch (e: Exception) {
            runOnUIThread {
                output?.onBootstrapError(ViewError(title = Translation.error.startupTitle, message = Translation.error.startupMessage, shouldCloseView = true))
            }
        }
    }
}