package dk.eboks.app.presentation.ui.screens.profile

import dk.eboks.app.domain.managers.AppStateManager
import dk.nodes.arch.presentation.base.BasePresenterImpl

class ProfilePresenter(val appStateManager: AppStateManager) : ProfileContract.Presenter,
                                                               BasePresenterImpl<ProfileContract.View>() {
}