package dk.eboks.app.presentation.ui.mail.overview

import dk.nodes.arch.presentation.base.BasePresenter
import dk.nodes.arch.presentation.base.BaseView

/**
 * Created by bison on 07-11-2017.
 */
interface MailOverviewContract {
    interface View : BaseView {
        fun showProgress(show: Boolean)
    }

    interface Presenter : BasePresenter<View> {
        fun refresh()
    }
}