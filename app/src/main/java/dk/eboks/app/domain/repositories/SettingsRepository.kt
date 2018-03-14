package dk.eboks.app.domain.repositories

import dk.eboks.app.domain.models.local.Settings

/**
 * Created by bison on 15/12/17.
 */
interface SettingsRepository {
    fun get() : Settings
    fun put(settings : Settings)
}