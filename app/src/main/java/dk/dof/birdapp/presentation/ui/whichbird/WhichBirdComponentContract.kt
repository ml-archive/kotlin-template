package dk.dof.birdapp.presentation.ui.whichbird

import dk.nodes.arch.presentation.base.BasePresenter
import dk.nodes.arch.presentation.base.BaseView

interface WhichBirdComponentContract {
    interface View : BaseView {

    }

    interface Presenter : BasePresenter<View> {
    }
}