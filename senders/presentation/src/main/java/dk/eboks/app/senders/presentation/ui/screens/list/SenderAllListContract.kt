package dk.eboks.app.senders.presentation.ui.screens.list

import dk.eboks.app.presentation.base.BaseView
import dk.nodes.arch.presentation.base.BasePresenter

/**
 * Created by bison on 07-11-2017.
 */
interface SenderAllListContract {
    interface View : BaseView

    interface Presenter : BasePresenter<View>
}