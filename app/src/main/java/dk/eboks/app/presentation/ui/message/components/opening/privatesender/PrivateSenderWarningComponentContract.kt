package dk.eboks.app.presentation.ui.message.components.opening.privatesender

import dk.eboks.app.presentation.base.BaseView
import dk.nodes.arch.presentation.base.BasePresenter

/**
 * Created by bison on 07-11-2017.
 */
interface PrivateSenderWarningComponentContract {
    interface View : BaseView {
        fun showOpeningProgress(show: Boolean)
    }

    interface Presenter : BasePresenter<View> {
        fun setShouldProceed(proceed: Boolean)
    }
}