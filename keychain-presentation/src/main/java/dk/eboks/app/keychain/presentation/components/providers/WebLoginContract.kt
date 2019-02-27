package dk.eboks.app.keychain.presentation.components.providers

import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.login.User
import dk.eboks.app.presentation.base.BaseView
import dk.nodes.arch.presentation.base.BasePresenter

/**
 * Created by bison on 07-11-2017.
 */
interface WebLoginContract {
    interface View : BaseView {
        fun setupLogin(user: User?)
        fun showError(viewError: ViewError)
        fun close()
        fun proceed()
        fun showMergeAcountDrawer()
        fun finishActivity(resultCode: Int? = null)
    }

    interface Presenter : BasePresenter<View> {
        fun setup()
        fun login(webToken: String)
        fun mergeAccountOrKeepSeparated()
        fun cancelAndClose()
    }
}