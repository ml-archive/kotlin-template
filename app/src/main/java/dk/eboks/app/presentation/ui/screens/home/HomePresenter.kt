package dk.eboks.app.presentation.ui.screens.home

import dk.eboks.app.domain.managers.AppStateManager
import dk.nodes.arch.presentation.base.BasePresenterImpl

/**
 * Created by bison on 20-05-2017.
 */
class HomePresenter(val appStateManager: AppStateManager) : HomeContract.Presenter, BasePresenterImpl<HomeContract.View>() {
    init {
    }

}