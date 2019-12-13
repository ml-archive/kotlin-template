package dk.nodes.template.domain.interactors

import dk.nodes.template.domain.managers.ThemeManager
import dk.nodes.template.models.Theme
import javax.inject.Inject

class GetThemeInteractor @Inject constructor(
    private val themeManager: ThemeManager
) : NoInputInteractor<Theme> {
    override suspend fun invoke(input: Unit) = themeManager.theme
}