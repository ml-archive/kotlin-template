package dk.nodes.template.presentation.util

import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import dk.nodes.template.domain.entities.Theme

object ThemeHelper {

    fun applyTheme(theme: Theme) {
        val systemTheme = when (theme) {
            Theme.LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
            Theme.DARK -> AppCompatDelegate.MODE_NIGHT_YES
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