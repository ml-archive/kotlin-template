package dk.eboks.app.presentation.ui.splash

import dk.nodes.arch.presentation.base.BasePresenter
import dk.nodes.arch.presentation.base.BaseView

/**
 * Created by bison on 07-11-2017.
 */
interface SplashContract {
    interface View : BaseView {
        fun performVersionControl()
        fun showError(msg : String)
    }

    interface Presenter : BasePresenter<View> {
        fun proceed()
    }
}