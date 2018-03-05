package dk.eboks.app.domain.interactors

import dk.eboks.app.domain.managers.*
import dk.eboks.app.domain.repositories.SettingsRepository
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor
import timber.log.Timber

/**
 * Created by bison on 24-06-2017.
 */
class BootstrapInteractorImpl(executor: Executor, val guidManager: GuidManager, val settingsRepository: SettingsRepository,
                              val protocolManager: ProtocolManager, val appStateManager: AppStateManager,
                              val fileCacheManager: FileCacheManager, val userManager: UserManager) : BaseInteractor(executor), BootstrapInteractor {
    override var output : BootstrapInteractor.Output? = null
    override var input : BootstrapInteractor.Input? = null

    override fun execute() {

        // we don't use input in this example but we could:
        input?.let {
            // do something with unwrapped input

        }
        val hasUsers = userManager.users.isNotEmpty()
        val settings = settingsRepository.get()
        if(settings.deviceId.isBlank()) {
            settings.deviceId = guidManager.generateGuid()
            Timber.d("No device ID found, generating new id: ${settings.deviceId}")
        }
        settingsRepository.put(settings)

        // Initialize eboks protocol
        protocolManager.init(settings.deviceId)

        try {
            runOnUIThread {
                output?.onBootstrapDone(hasUsers)
            }
        } catch (e: Exception) {
            runOnUIThread {
                output?.onBootstrapError(e.message ?: "Unknown error")
            }
        }
    }
}