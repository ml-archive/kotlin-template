package dk.eboks.app.presentation.ui.screens.channels

import dk.eboks.app.domain.models.channel.Channel
import dk.nodes.arch.presentation.base.BasePresenter
import dk.nodes.arch.presentation.base.BaseView

/**
 * Created by bison on 07-11-2017.
 */
interface ChannelsContract {
    interface View : BaseView {
        fun showError(msg : String)
    }

    interface Presenter : BasePresenter<View> {
    }
}