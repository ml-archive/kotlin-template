package dk.eboks.app.presentation.ui.mail.screens.overview

import dk.eboks.app.domain.models.login.User
import dk.eboks.app.presentation.base.BaseView
import dk.nodes.arch.presentation.base.BasePresenter

/**
 * Created by bison on 07-11-2017.
 */
interface MailOverviewContract {
    interface View : BaseView {
        fun showProgress(show: Boolean)
        fun setUser(user: User?, userName: String?)
    }

    interface Presenter : BasePresenter<View> {
        fun refresh()
    }
}