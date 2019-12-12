package dk.nodes.template.domain.interactors

import android.util.Log
import dk.nodes.template.domain.managers.PrefManager
import dk.nodes.template.models.Theme
import javax.inject.Inject

class SetThemeInteractor @Inject constructor(
    private val prefManager: PrefManager
) : NoOutputInteractor<Theme> {

    companion object {
        private const val PREF_THEME = "pref_theme"
    }

    override suspend fun invoke(input: Theme) {
        Log.d("LOGTHEME", "SALVO PREF ${input.name}")
        prefManager.setString(PREF_THEME, input.name)
    }
}