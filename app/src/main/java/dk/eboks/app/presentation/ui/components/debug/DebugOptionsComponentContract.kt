package dk.eboks.app.presentation.ui.components.debug

import dk.nodes.arch.presentation.base.BasePresenter
import dk.eboks.app.presentation.base.BaseView

/**
 * Created by bison on 07-11-2017.
 */
interface DebugOptionsComponentContract {
    interface View : BaseView {
        fun showCountrySpinner(configIndex : Int)
    }

    interface Presenter : BasePresenter<View> {
        fun setup()
        fun setConfig(name : String)
    }
}