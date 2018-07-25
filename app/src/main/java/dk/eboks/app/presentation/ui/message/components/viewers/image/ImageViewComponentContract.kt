package dk.eboks.app.presentation.ui.message.components.viewers.image

import dk.eboks.app.presentation.base.BaseView
import dk.nodes.arch.presentation.base.BasePresenter

/**
 * Created by bison on 07-11-2017.
 */
interface ImageViewComponentContract {
    interface View : BaseView {
        fun showImage(filename : String)
        fun showImageURI(uri : String)
    }

    interface Presenter : BasePresenter<View> {
        fun setup(uriString : String?)
    }
}