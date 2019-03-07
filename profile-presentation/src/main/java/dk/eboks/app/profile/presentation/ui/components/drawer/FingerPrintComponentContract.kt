package dk.eboks.app.profile.presentation.ui.components.drawer

import dk.eboks.app.domain.models.login.LoginInfo
import dk.eboks.app.domain.models.login.LoginInfoType
import dk.eboks.app.presentation.base.BaseView
import dk.nodes.arch.presentation.base.BasePresenter

/**
 * Created by bison on 07-11-2017.
 */
interface FingerPrintComponentContract {

    interface View : BaseView {
        fun finishView()
        fun getUserLoginInfo(): LoginInfo
        fun setProviderMode(mode: LoginInfoType)
        fun finishActivity(resultCode: Int?)
        fun proceedAfterLoginVerification()
        fun showProgress(show: Boolean)
    }

    interface Presenter : BasePresenter<View> {
        fun verifyLoginCredentials()
        fun encryptUserLoginInfo()
    }
}