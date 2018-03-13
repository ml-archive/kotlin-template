package dk.nodes.template.injection.modules

import dagger.Module
import dagger.Provides
import dk.nodes.arch.domain.injection.scopes.ActivityScope
import dk.nodes.template.domain.interactors.GetPostsInteractor
import dk.nodes.template.presentation.ui.main.MainContract
import dk.nodes.template.presentation.ui.main.MainPresenter

/**
 * Created by bison on 06/12/17.
 */
@Module
class PresentationModule {
    @ActivityScope
    @Provides
    fun provideMainPresenter(getPostsInteractor: GetPostsInteractor): MainContract.Presenter {
        return MainPresenter(getPostsInteractor)
    }
}