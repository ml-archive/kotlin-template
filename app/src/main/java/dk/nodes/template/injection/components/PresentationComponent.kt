package dk.nodes.template.injection.components

import dagger.Subcomponent
import dk.nodes.arch.domain.injection.scopes.ActivityScope
import dk.nodes.template.injection.modules.PresentationModule
import dk.nodes.template.presentation.ui.main.MainActivity
import dk.nodes.template.presentation.ui.main.MainPresenter

@Subcomponent(modules = [PresentationModule::class])
@ActivityScope
interface PresentationComponent {
    fun inject(target: MainActivity)
    fun inject(target: MainPresenter)
}
