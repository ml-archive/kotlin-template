package dk.eboks.app.presentation.ui.screens.main

import dk.nodes.arch.presentation.base.BasePresenter
import dk.nodes.arch.presentation.base.BaseView

/**
 * Created by bison on 07-11-2017.
 */
interface MainContract {
    interface View : BaseView {
        fun showError(msg : String)
    }

    interface Presenter : BasePresenter<MainContract.View> {
    }
}