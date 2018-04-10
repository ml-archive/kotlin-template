package dk.eboks.app.presentation.ui.screens.uploads

import dk.nodes.arch.presentation.base.BasePresenter
import dk.eboks.app.presentation.base.BaseView

/**
 * Created by bison on 07-11-2017.
 */
interface UploadsContract {
    interface View : BaseView {
    }

    interface Presenter : BasePresenter<View> {
    }
}