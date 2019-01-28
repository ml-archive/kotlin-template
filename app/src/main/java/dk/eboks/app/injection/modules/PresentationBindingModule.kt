package dk.eboks.app.injection.modules

import dagger.Binds
import dagger.Module
import dk.eboks.app.presentation.ui.home.screens.HomeContract
import dk.eboks.app.presentation.ui.home.screens.HomePresenter
import dk.nodes.arch.domain.injection.scopes.ActivityScope

@Module
abstract class PresentationBindingModule {
    @ActivityScope
    @Binds
    abstract fun provideHomePresenter(presenter: HomePresenter): HomeContract.Presenter
}