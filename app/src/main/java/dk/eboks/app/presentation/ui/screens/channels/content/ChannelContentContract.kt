package dk.eboks.app.presentation.ui.screens.channels.content

import dk.eboks.app.presentation.base.BaseView
import dk.nodes.arch.presentation.base.BasePresenter

/**
 * Created by bison on 07-11-2017.
 */
interface ChannelContentContract {
    interface View : BaseView {
        fun finish()
    }

    interface Presenter : BasePresenter<View> {
    }
}