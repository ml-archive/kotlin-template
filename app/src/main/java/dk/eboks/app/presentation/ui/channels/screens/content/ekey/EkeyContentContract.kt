package dk.eboks.app.presentation.ui.channels.screens.content.ekey

import dk.eboks.app.presentation.base.BaseView
import dk.nodes.arch.presentation.base.BasePresenter

/**
 * Created by bison on 07-11-2017.
 */
interface EkeyContentContract {
    interface View : BaseView {
    }

    interface Presenter : BasePresenter<View> {
        fun getMasterKey(): String?
    }
}