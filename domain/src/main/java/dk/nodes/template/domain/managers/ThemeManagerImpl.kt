package dk.nodes.template.domain.managers

import dk.nodes.template.domain.models.Theme
import javax.inject.Inject

class ThemeManagerImpl @Inject constructor(private val prefManager: PrefManager) : ThemeManager {

    override var theme: Theme
        get() = getThemePref()
        set(value) {
            setThemePRef(value)
        }

    companion object {
        private const val PREF_THEME = "PREF_THEME"
    }

    private fun getThemePref(): Theme {
        val t = prefManager.getString(PREF_THEME, null)
        return if (t == null) Theme.LIGHT else Theme.valueOf(t)
    }

    private fun setThemePRef(theme: Theme) {
        prefManager.setString(PREF_THEME, theme.name)
    }
}