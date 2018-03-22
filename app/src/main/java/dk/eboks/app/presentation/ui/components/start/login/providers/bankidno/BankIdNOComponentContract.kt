package dk.eboks.app.presentation.ui.components.start.login.providers.bankidno

import dk.eboks.app.domain.models.login.User
import dk.nodes.arch.presentation.base.BasePresenter
import dk.eboks.app.presentation.base.BaseView

/**
 * Created by bison on 07-11-2017.
 */
interface BankIdNOComponentContract {
    interface View : BaseView {
        fun setupLogin(user : User)
        fun close()
        fun proceed()
    }

    interface Presenter : BasePresenter<View> {
        fun setup()
        fun login(user : User)
        fun cancelAndClose()
    }
}