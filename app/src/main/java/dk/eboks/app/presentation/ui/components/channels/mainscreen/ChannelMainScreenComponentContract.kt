package dk.eboks.app.presentation.ui.components.channels.mainscreen

import dk.nodes.arch.presentation.base.BasePresenter
import dk.nodes.arch.presentation.base.BaseView

/**
 * Created by bison on 07-11-2017.
 */
interface ChannelMainScreenComponentContract {
    interface View : BaseView {

    }

    interface Presenter : BasePresenter<View> {
    }
}