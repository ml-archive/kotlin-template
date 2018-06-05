package dk.dof.birdapp.presentation.ui.map

import dk.nodes.arch.presentation.base.BasePresenter
import dk.nodes.arch.presentation.base.BaseView

interface MapComponentContract {
    interface View : BaseView {

    }

    interface Presenter : BasePresenter<View> {
    }
}