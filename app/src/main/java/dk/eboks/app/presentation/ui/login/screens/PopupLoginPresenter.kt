package dk.eboks.app.presentation.ui.login.screens

import dk.eboks.app.domain.managers.AppStateManager
import dk.nodes.arch.presentation.base.BasePresenterImpl

/**
 * Created by bison on 20-05-2017.
 */
class PopupLoginPresenter(val appStateManager: AppStateManager) : PopupLoginContract.Presenter, BasePresenterImpl<PopupLoginContract.View>() {
    init {
    }


}