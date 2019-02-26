package cz.levinzonr.keychain.presentation.components

import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.presentation.base.BaseView
import dk.nodes.arch.presentation.base.BasePresenter

/**
 * Created by bison on 07-11-2017.
 */
interface ForgotPasswordComponentContract {
    interface View : BaseView {
        fun showSuccess()
        fun showError(viewError: ViewError)
    }

    interface Presenter : BasePresenter<View> {
        fun resetPassword(email: String)
    }
}