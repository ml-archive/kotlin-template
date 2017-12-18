package dk.eboks.app.injection.components

import dagger.Component
import dagger.Module
import dagger.Provides
import dk.eboks.app.injection.modules.PresentationModule
import dk.nodes.arch.domain.injection.scopes.ActivityScope

/**
 * Created by bison on 26-07-2017.
 */

@Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(PresentationModule::class))
@ActivityScope
interface PresentationComponent {
    fun inject(target : dk.eboks.app.presentation.ui.main.MainActivity)
    fun inject(target : dk.eboks.app.presentation.ui.main.MainPresenter)

}
