package dk.eboks.app.domain.interactors

import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.managers.GuidManager
import dk.eboks.app.domain.managers.ProtocolManager
import dk.eboks.app.domain.repositories.SettingsRepository
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor
import timber.log.Timber

/**
 * Created by bison on 24-06-2017.
 */
class BootstrapInteractorImpl(executor: Executor, val guidManager: GuidManager, val settingsRepository: SettingsRepository, val protocolManager: ProtocolManager, val appStateManager: AppStateManager) : BaseInteractor(executor), BootstrapInteractor {
    override var output : BootstrapInteractor.Output? = null
    override var input : BootstrapInteractor.Input? = null

    override fun execute() {

        // we don't use input in this example but we could:
        input?.let {
            // do something with unwrapped input

        }

        val settings = settingsRepository.get()
        if(settings.deviceId.isBlank()) {
            settings.deviceId = guidManager.generateGuid()
            Timber.d("No device ID found, generating new id: ${settings.deviceId}")
        }
        settingsRepository.put(settings)

        // Initialize eboks protocol
        protocolManager.init(settings.deviceId)

        executor.sleepUntilSignalled("bootstrapDone")

        try {
            runOnUIThread {
                output?.onBootstrapDone()
            }
        } catch (e: Exception) {
            runOnUIThread {
                output?.onBootstrapError(e.message ?: "Unknown error")
            }
        }
    }
}