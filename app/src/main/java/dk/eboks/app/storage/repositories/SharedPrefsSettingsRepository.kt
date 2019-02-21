package dk.eboks.app.storage.repositories

import dk.eboks.app.domain.managers.PrefManager
import dk.eboks.app.domain.models.local.Settings
import dk.eboks.app.domain.repositories.SettingsRepository

/**
 * Created by bison on 15/12/17.
 */
class SharedPrefsSettingsRepository(val prefManager: PrefManager) :
    SettingsRepository {
    override fun get(): Settings {
        return Settings(prefManager.getString("deviceId", "")!!)
    }

    override fun put(settings: Settings) {
        prefManager.setString("deviceId", settings.deviceId)
    }
}