package dk.eboks.app.presentation.ui.profile.screens

import dk.eboks.app.presentation.base.BaseView
import dk.nodes.arch.presentation.base.BasePresenter

interface ProfileContract {
    interface View : BaseView

    interface Presenter : BasePresenter<View>
}