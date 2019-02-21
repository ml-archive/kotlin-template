package dk.eboks.app.domain.interactors.user

import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.managers.UserSettingsManager
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor

/**
 * @author Christian
 * @since 6/19/2018.
 */
class SaveUserSettingsInteractorImpl(
        executor: Executor,
        val appState: AppStateManager,
        val userSettingsManager: UserSettingsManager
) : BaseInteractor(executor), SaveUserSettingsInteractor {
    override var output: SaveUserSettingsInteractor.Output? = null
    override var input: SaveUserSettingsInteractor.Input? = null

    override fun execute() {
        // we don't use input in this example but we could:
        try {
            input?.userSettings?.let { settings ->
                userSettingsManager.put(settings)
                appState.state?.currentSettings = settings
                runOnUIThread {
                    output?.onSaveSettings()
                }
            }
        } catch (t: Throwable) {
        }
    }
}