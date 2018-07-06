package dk.eboks.app.presentation.ui.navigation.components

import dk.eboks.app.domain.managers.AppStateManager
import dk.nodes.arch.presentation.base.BasePresenterImpl
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class NavBarComponentPresenter @Inject constructor(val appState: AppStateManager) : NavBarComponentContract.Presenter, BasePresenterImpl<NavBarComponentContract.View>() {

    init {
    }

}