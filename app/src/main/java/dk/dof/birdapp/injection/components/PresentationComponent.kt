package dk.dof.birdapp.injection.components

import dagger.Component
import dagger.Subcomponent
import dk.dof.birdapp.injection.modules.PresentationModule
import dk.dof.birdapp.presentation.ui.birdbook.BirdbookComponentFragment
import dk.dof.birdapp.presentation.ui.main.MainActivity
import dk.dof.birdapp.presentation.ui.main.MainPresenter
import dk.dof.birdapp.presentation.ui.map.MapComponentFragment
import dk.dof.birdapp.presentation.ui.navigation.NavigationBarFragment
import dk.dof.birdapp.presentation.ui.whichbird.WhichBirdComponentFragment
import dk.dof.birdapp.presentation.ui.whichbird.selectorDetailView.birdsize.BirdSizeSelectorFragment
import dk.nodes.arch.domain.injection.scopes.ActivityScope


/**
 * Created by bison on 26-07-2017.
 */

@Subcomponent(modules = arrayOf(PresentationModule::class))
@ActivityScope
interface PresentationComponent {
    fun inject(target: MainActivity)

    fun inject(target: NavigationBarFragment)

    // map
    fun inject(target: MapComponentFragment)

    // birdbook
    fun inject(target: BirdbookComponentFragment)

    //whichbird
    fun inject(target: WhichBirdComponentFragment)
    fun inject(target: BirdSizeSelectorFragment)
}
