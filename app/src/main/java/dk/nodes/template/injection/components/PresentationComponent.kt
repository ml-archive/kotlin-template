package dk.nodes.template.injection.components

import dagger.Component
import dagger.Module
import dagger.Provides
import dk.nodes.arch.domain.injection.scopes.ActivityScope
import dk.nodes.template.injection.modules.PresentationModule
import dk.nodes.template.presentation.ui.main.MainActivity
import dk.nodes.template.presentation.ui.main.MainPresenter

/**
 * Created by bison on 26-07-2017.
 */

@Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(PresentationModule::class))
@ActivityScope
interface PresentationComponent {
    fun inject(target : MainActivity)
    fun inject(target : MainPresenter)

}
