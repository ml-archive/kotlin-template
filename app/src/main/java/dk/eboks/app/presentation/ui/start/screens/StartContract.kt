package dk.eboks.app.presentation.ui.start.screens

import dk.eboks.app.presentation.base.BaseView
import dk.nodes.arch.presentation.base.BasePresenter

/**
 * Created by bison on 07-11-2017.
 */
interface StartContract {
    interface View : BaseView {
        fun performVersionControl()
        fun startMain()
        fun showWelcomeComponent()
        fun showUserCarouselComponent()
    }

    interface Presenter : BasePresenter<View> {
        fun startup()
        fun proceed()
    }
}