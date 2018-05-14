package dk.eboks.app.presentation.ui.screens.channels.content.storebox

import dk.eboks.app.presentation.base.BaseView
import dk.nodes.arch.presentation.base.BasePresenter

/**
 * Created by Christian on 5/14/2018.
 * @author   Christian
 * @since    5/14/2018.
 */
interface ConnectStoreboxContract {

    interface View : BaseView {
        fun showFound()
        fun showNotFound()
    }

    interface Presenter : BasePresenter<View> {
        fun signIn(email: String, password: String)
        fun confirm(code: String)
    }
}