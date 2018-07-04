package dk.eboks.app.presentation.ui.message.components.viewers.text

import dk.eboks.app.presentation.base.BaseView
import dk.nodes.arch.presentation.base.BasePresenter

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