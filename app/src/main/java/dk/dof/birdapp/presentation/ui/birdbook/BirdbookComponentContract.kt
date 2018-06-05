package dk.dof.birdapp.presentation.ui.birdbook

import dk.nodes.arch.presentation.base.BasePresenter
import dk.nodes.arch.presentation.base.BaseView

interface BirdbookComponentContract {
    interface View : BaseView {

    }

    interface Presenter : BasePresenter<View> {
    }
}