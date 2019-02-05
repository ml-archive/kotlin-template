package dk.eboks.app.storage.managers

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dk.eboks.app.domain.managers.UserSettingsManager
import dk.eboks.app.domain.models.login.UserSettings
import dk.eboks.app.storage.base.GsonFileStorageRepository
import timber.log.Timber

/**
 * @author Christian
 * @since 6/19/2018.
 */
class UserSettingsManagerImpl(val context: Context, val gson: Gson) : UserSettingsManager {
    private var userSettings = mutableMapOf<Int, UserSettings>()
    private val settingsStore = UserSettingsStore()

    init {
        val type = object : TypeToken<MutableMap<Int, UserSettings>>() {}.type
        try {
            userSettings = settingsStore.load(type)
            Timber.i("Loaded settings store with ${userSettings.size} entries")

            for (entry in userSettings) {
                Timber.d("Entry: ${entry.value}")
            }
        } catch (t: Throwable) {
            Timber.w("creating empty settings store")
            save()
        }
    }

    override fun get(id: Int): UserSettings {
        userSettings[id]?.let {
            return it
        }
        return UserSettings(id = id)
    }

    override fun put(settings: UserSettings): UserSettings {
        userSettings[settings.id] = settings
        Timber.i("put $settings added")
        save()
        return settings
    }

    override fun remove(settings: UserSettings) {
        userSettings.remove(settings.id)
        Timber.i("remove $settings removed")
        save()
    }

    override fun save() {
        settingsStore.save(userSettings)
        Timber.i("Settings saved")
    }

    override fun removeFingerprintFromYall() {
        for (kv in userSettings) {
            kv.value.hasFingerprint = false
        }
    }

    inner class UserSettingsStore :
        GsonFileStorageRepository<MutableMap<Int, UserSettings>>(context, gson, "usersettings.json")
}