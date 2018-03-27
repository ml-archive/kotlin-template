package dk.eboks.app.presentation.ui.screens.profile

import dk.nodes.arch.presentation.base.BasePresenter
import dk.eboks.app.presentation.base.BaseView

interface ProfileContract {
    interface View : BaseView {
    }

    interface Presenter : BasePresenter<View> {
    }
}