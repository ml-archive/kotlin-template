package dk.eboks.app.profile.presentation.ui.screens

import dk.nodes.arch.presentation.base.BasePresenterImpl
import javax.inject.Inject

internal class ProfilePresenter @Inject constructor() : ProfileContract.Presenter,
    BasePresenterImpl<ProfileContract.View>()