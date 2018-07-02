package dk.eboks.app.presentation.ui.components.message.viewers.image

import dk.eboks.app.presentation.base.BaseView
import dk.nodes.arch.presentation.base.BasePresenter

/**
 * Created by bison on 07-11-2017.
 */
interface ImageViewComponentContract {
    interface View : BaseView {
        fun showImage(filename : String)
    }

    interface Presenter : BasePresenter<View> {
    }
}