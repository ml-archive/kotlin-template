package dk.eboks.app.presentation.ui.message.components.opening.promulgation

import dk.eboks.app.presentation.base.BaseView
import dk.nodes.arch.presentation.base.BasePresenter

/**
 * Created by bison on 07-11-2017.
 */
interface PromulgationComponentContract {
    interface View : BaseView {
        fun setPromulgationHeader(text : String)
        fun setPromulgationText(promulgationText: String)
    }

    interface Presenter : BasePresenter<View> {
        fun setShouldProceed(proceed : Boolean)
    }
}