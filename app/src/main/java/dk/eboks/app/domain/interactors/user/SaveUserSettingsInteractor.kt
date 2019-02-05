package dk.eboks.app.domain.interactors.user

import dk.eboks.app.domain.models.login.UserSettings
import dk.nodes.arch.domain.interactor.Interactor

/**
 * @author Christian
 * @since 6/19/2018.
 */
interface SaveUserSettingsInteractor : Interactor {
    var input: Input?
    var output: Output?

    data class Input(val userSettings: UserSettings)

    interface Output {
        fun onSaveSettings()
    }
}