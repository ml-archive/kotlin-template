package dk.nodes.template.domain.interactors

import dk.nodes.template.domain.managers.ThemeManager
import dk.nodes.template.models.Theme
import javax.inject.Inject

class SetThemeInteractor @Inject constructor(
    private val themeManager: ThemeManager
) : NoOutputInteractor<Theme> {
    override suspend fun invoke(input: Theme) {
        themeManager.theme = input
    }
}