package dk.eboks.app.presentation.ui.components.message.opening.receipt

import dk.eboks.app.presentation.base.BaseView
import dk.nodes.arch.presentation.base.BasePresenter

/**
 * Created by bison on 07-11-2017.
 */
interface OpeningReceiptComponentContract {
    interface View : BaseView {
        fun showOpeningProgress(show : Boolean)
    }

    interface Presenter : BasePresenter<View> {
        fun setShouldProceed(proceed : Boolean)
    }
}