package dk.nodes.template.domain.interactors

import android.util.Log
import dk.nodes.template.domain.managers.PrefManager
import dk.nodes.template.models.Theme
import javax.inject.Inject

class GetThemeInteractor @Inject constructor(
    private val prefManager: PrefManager
) : NoInputInteractor<Theme> {

    companion object {
        private const val PREF_THEME = "pref_theme"
    }

    override suspend fun invoke(input: Unit): Theme {
        val t = prefManager.getString(PREF_THEME, null)
        Log.d("LOGTHEME", "GET" + (if (t == null) Theme.LIGHT else Theme.valueOf(t)).toString())
        return if (t == null) Theme.LIGHT else Theme.valueOf(t)
    }
}