package dk.eboks.app.injection.modules

import dagger.Module
import dagger.Provides
import dk.nodes.arch.domain.injection.scopes.ActivityScope

/**
 * Created by bison on 07/12/17.
 */

@Module
class PresentationModule {
    @ActivityScope
    @Provides
    fun provideMainPresenter(getPostsInteractor: dk.eboks.app.domain.interactors.LoginInteractor) : dk.eboks.app.presentation.ui.main.MainContract.Presenter {
        return dk.eboks.app.presentation.ui.main.MainPresenter(getPostsInteractor)
    }
}