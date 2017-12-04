package dk.eboks.app.presentation.injection

import dagger.Component
import dagger.Module
import dagger.Provides
import dk.nodes.arch.domain.injection.scopes.ActivityScope

/**
 * Created by bison on 26-07-2017.
 */

@Component(dependencies = arrayOf(dk.eboks.app.injection.AppComponent::class), modules = arrayOf(dk.eboks.app.presentation.injection.PresentationModule::class))
@ActivityScope
interface PresentationComponent {
    fun inject(target : dk.eboks.app.presentation.ui.main.MainActivity)
    fun inject(target : dk.eboks.app.presentation.ui.main.MainPresenter)

}

@Module
class PresentationModule {
    @ActivityScope
    @Provides fun provideMainPresenter(getPostsInteractor: dk.eboks.app.domain.interactors.GetPostsInteractor) : dk.eboks.app.presentation.ui.main.MainContract.Presenter {
        return dk.eboks.app.presentation.ui.main.MainPresenter(getPostsInteractor)
    }
}