package dk.eboks.app.presentation.ui.screens.profile

import dk.eboks.app.presentation.base.BaseView
import dk.nodes.arch.presentation.base.BasePresenter

interface ProfileContract {
    interface View : BaseView {
    }

    interface Presenter : BasePresenter<View> {
    }
}