package dk.eboks.app.presentation.ui.components.debug

import dk.eboks.app.domain.models.login.LoginState
import dk.nodes.arch.presentation.base.BasePresenter
import dk.eboks.app.presentation.base.BaseView

/**
 * Created by Christian on 6/15/2018.
 * @author   Christian
 * @since    6/15/2018.
 */
interface DebugUsersComponentContract {
    interface View : BaseView {
        fun showUsers(users : List<LoginState>)
    }

    interface Presenter : BasePresenter<View> {
        fun makeList()
        fun updateLoginState(ls : LoginState)
    }
}