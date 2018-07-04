package dk.eboks.app.presentation.ui.overlay.screens

import dk.eboks.app.domain.managers.AppStateManager
import dk.nodes.arch.presentation.base.BasePresenterImpl

/**
 * Created by bison on 20-05-2017.
 */
class OverlayPresenter(val appStateManager: AppStateManager) : OverlayContract.Presenter, BasePresenterImpl<OverlayContract.View>() {
    init {
    }

}