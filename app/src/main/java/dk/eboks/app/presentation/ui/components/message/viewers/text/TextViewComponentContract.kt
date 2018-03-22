package dk.eboks.app.presentation.ui.components.message.viewers.text

import dk.nodes.arch.presentation.base.BasePresenter
import dk.eboks.app.presentation.base.BaseView

/**
 * Created by bison on 07-11-2017.
 */
interface TextViewComponentContract {
    interface View : BaseView {
        fun showText(filename: String)
    }

    interface Presenter : BasePresenter<View> {
    }
}