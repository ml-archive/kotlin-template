package dk.eboks.app.presentation.ui.components.message.viewers.image

import dk.nodes.arch.presentation.base.BasePresenter
import dk.eboks.app.presentation.base.BaseView

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