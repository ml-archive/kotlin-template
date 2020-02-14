package dk.nodes.template.domain.interactors

import dk.nodes.template.domain.managers.ThemeManager
import dk.nodes.template.domain.entities.Theme
import javax.inject.Inject

class SwitchThemeInteractor @Inject constructor(
    private val themeManager: ThemeManager
) : NoInputInteractor<Theme> {
    override suspend fun invoke(input: Unit): Theme {
        themeManager.theme = if (themeManager.theme == Theme.LIGHT) Theme.DARK else Theme.LIGHT
        return themeManager.theme
    }
}