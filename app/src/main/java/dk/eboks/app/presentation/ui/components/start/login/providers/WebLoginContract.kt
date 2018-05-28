package dk.eboks.app.presentation.ui.components.start.login.providers

import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.login.User
import dk.nodes.arch.presentation.base.BasePresenter
import dk.eboks.app.presentation.base.BaseView

/**
 * Created by bison on 07-11-2017.
 */
interface WebLoginContract {
    interface View : BaseView {
        fun setupLogin(user : User)
        fun showError(viewError: ViewError)
        fun close()
        fun proceed()
    }

    interface Presenter : BasePresenter<View> {
        fun setup()
        fun login(webToken : String)
        fun cancelAndClose()
    }
}