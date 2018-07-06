package dk.eboks.app.presentation.ui.profile.screens.myinfo

import dk.eboks.app.domain.managers.AppStateManager
import dk.nodes.arch.presentation.base.BasePresenterImpl

/**
 * Created by bison on 20-05-2017.
 */
class MyInfoPresenter(val appStateManager: AppStateManager) : MyInfoContract.Presenter, BasePresenterImpl<MyInfoContract.View>() {
    init {
    }

}