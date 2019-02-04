package dk.eboks.app.presentation.ui.profile.screens

import dk.eboks.app.domain.managers.AppStateManager
import dk.nodes.arch.presentation.base.BasePresenterImpl

class ProfilePresenter(val appStateManager: AppStateManager) : ProfileContract.Presenter,
    BasePresenterImpl<ProfileContract.View>()