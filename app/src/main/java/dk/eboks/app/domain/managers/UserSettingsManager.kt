package dk.eboks.app.domain.managers

import dk.eboks.app.domain.models.login.UserSettings


/**
 * @author   Christian
 * @since    6/19/2018.
 */
interface UserSettingsManager {
    fun get(id: Int): UserSettings
    fun put(settings: UserSettings): UserSettings
    fun remove(settings : UserSettings)
    fun save()
}