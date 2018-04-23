package dk.eboks.app.presentation.ui.components.message.opening.privatesender

import dk.nodes.arch.presentation.base.BasePresenter
import dk.eboks.app.presentation.base.BaseView

/**
 * Created by bison on 07-11-2017.
 */
interface PrivateSenderWarningComponentContract {
    interface View : BaseView {
        fun showOpeningProgress(show : Boolean)
    }

    interface Presenter : BasePresenter<View> {
        fun setShouldProceed(proceed : Boolean)
    }
}