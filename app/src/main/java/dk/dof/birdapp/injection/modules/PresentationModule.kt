package dk.dof.birdapp.injection.modules

import dagger.Module
import dagger.Provides
import dk.dof.birdapp.domain.interactors.GetPostsInteractor
import dk.dof.birdapp.presentation.ui.birdbook.BirdbookComponentContract
import dk.dof.birdapp.presentation.ui.birdbook.BirdbookComponentPresenter
import dk.dof.birdapp.presentation.ui.main.MainContract
import dk.dof.birdapp.presentation.ui.main.MainPresenter
import dk.dof.birdapp.presentation.ui.map.MapComponentContract
import dk.dof.birdapp.presentation.ui.map.MapComponentPresenter
import dk.dof.birdapp.presentation.ui.navigation.NavigationBarContract
import dk.dof.birdapp.presentation.ui.navigation.NavigationBarPresenter
import dk.dof.birdapp.presentation.ui.whichbird.WhichBirdComponentContract
import dk.dof.birdapp.presentation.ui.whichbird.WhichBirdComponentPresenter
import dk.dof.birdapp.presentation.ui.whichbird.selectorDetailView.birdsize.BirdSizeSelectorContract
import dk.dof.birdapp.presentation.ui.whichbird.selectorDetailView.birdsize.BirdSizeSelectorPresenter
import dk.nodes.arch.domain.injection.scopes.ActivityScope


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

    @Provides
    fun providesBirdSizeSelectorPresenter(): BirdSizeSelectorContract.Presenter {
        return BirdSizeSelectorPresenter()
    }

    @Provides
    fun providesBirdbookComponentPresenter(): BirdbookComponentContract.Presenter {
        return BirdbookComponentPresenter()
    }

    @Provides
    fun providesWhichBirdComponentPresenter(): WhichBirdComponentContract.Presenter {
        return WhichBirdComponentPresenter()
    }

    @Provides
    fun providesMapComponentPresenter(): MapComponentContract.Presenter {
        return MapComponentPresenter()
    }

    @Provides
    fun providesNavigationBarPresenter(): NavigationBarContract.Presenter {
        return NavigationBarPresenter()
    }
}