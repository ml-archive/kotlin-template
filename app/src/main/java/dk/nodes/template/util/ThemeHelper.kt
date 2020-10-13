package dk.nodes.template.util

import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import dk.nodes.template.core.entities.Theme

object ThemeHelper {

    fun applyTheme(theme: dk.nodes.template.core.entities.Theme) {
        val systemTheme = when (theme) {
            dk.nodes.template.core.entities.Theme.LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
            dk.nodes.template.core.entities.Theme.DARK -> AppCompatDelegate.MODE_NIGHT_YES
            else -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                } else {
                    AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY
                }
            }
        }
        AppCompatDelegate.setDefaultNightMode(systemTheme)
    }
}