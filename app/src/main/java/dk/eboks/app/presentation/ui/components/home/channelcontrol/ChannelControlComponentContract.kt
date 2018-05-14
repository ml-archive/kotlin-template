package dk.eboks.app.presentation.ui.components.home.channelcontrol

import dk.nodes.arch.presentation.base.BasePresenter
import dk.eboks.app.presentation.base.BaseView

/**
 * Created by bison on 07-11-2017.
 */
interface ChannelControlComponentContract {
    interface View : BaseView {

    }

    interface Presenter : BasePresenter<View> {
        fun setup()
    }
}