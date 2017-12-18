package dk.eboks.app.domain.interactors

import dk.eboks.app.domain.managers.GuidManager
import dk.eboks.app.domain.repositories.SettingsRepository
import dk.eboks.app.network.Api
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor

/**
 * Created by bison on 24-06-2017.
 */
class BootstrapInteractorImpl(executor: Executor, val api: Api, val guidManager: GuidManager, val settingsRepository: SettingsRepository) : BaseInteractor(executor), BootstrapInteractor {
    override var output : BootstrapInteractor.Output? = null
    override var input : BootstrapInteractor.Input? = null

    override fun execute() {
        // we don't use input in this example but we could:
        input?.let {
            // do something with unwrapped input

        }

        val settings = settingsRepository.get()
        settings.deviceId = guidManager.generateGuid()
        settingsRepository.put(settings)

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