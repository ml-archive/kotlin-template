package dk.eboks.app.presentation.ui.components.message.viewers.html

import dk.nodes.arch.presentation.base.BasePresenter
import dk.nodes.arch.presentation.base.BaseView

/**
 * Created by bison on 07-11-2017.
 */
interface HtmlViewComponentContract {
    interface View : BaseView {
        fun showHtml(filename : String)
    }

    interface Presenter : BasePresenter<View> {
    }
}