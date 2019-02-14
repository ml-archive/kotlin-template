package dk.eboks.app.presentation.ui.profile.screens

import dk.nodes.arch.presentation.base.BasePresenterImpl
import javax.inject.Inject

class ProfilePresenter @Inject constructor() : ProfileContract.Presenter,
    BasePresenterImpl<ProfileContract.View>()