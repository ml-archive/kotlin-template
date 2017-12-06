package dk.nodes.template.presentation.injection

import dagger.Component
import dagger.Module
import dagger.Provides
import dk.nodes.arch.domain.injection.scopes.ActivityScope
import dk.nodes.template.domain.interactors.GetPostsInteractor
import dk.nodes.template.injection.components.AppComponent
import dk.nodes.template.presentation.ui.main.MainActivity
import dk.nodes.template.presentation.ui.main.MainContract
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

@Module
class PresentationModule {
    @ActivityScope
    @Provides fun provideMainPresenter(getPostsInteractor: GetPostsInteractor) : MainContract.Presenter {
        return MainPresenter(getPostsInteractor)
    }
}