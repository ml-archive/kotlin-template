package dk.dof.birdapp.presentation.ui.navigation

import dk.nodes.arch.presentation.base.BasePresenter
import dk.nodes.arch.presentation.base.BaseView

interface NavigationBarContract {
    interface View : BaseView {

    }

    interface Presenter : BasePresenter<View> {
    }
}