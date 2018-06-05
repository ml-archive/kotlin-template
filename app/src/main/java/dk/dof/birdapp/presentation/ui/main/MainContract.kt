package dk.dof.birdapp.presentation.ui.main

import dk.nodes.arch.presentation.base.BasePresenter
import dk.nodes.arch.presentation.base.BaseView

/**
 * Created by bison on 07-11-2017.
 */
interface MainContract {
    interface View : BaseView {

    }

    interface Presenter : BasePresenter<View> {
    }
}