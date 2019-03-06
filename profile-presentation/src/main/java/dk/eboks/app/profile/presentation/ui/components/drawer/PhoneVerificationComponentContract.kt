package dk.eboks.app.profile.presentation.ui.components.drawer

import dk.eboks.app.presentation.base.BaseView
import dk.nodes.arch.presentation.base.BasePresenter

/**
 * Created by bison on 07-11-2017.
 */
interface PhoneVerificationComponentContract {
    interface View : BaseView {
        fun showNumber(mobile: String)
        fun finishActivity(resultCode: Int?)
        fun showProgress(show: Boolean)
    }

    interface Presenter : BasePresenter<View> {
        fun setup(mobile: String)
        fun resendVerificationCode()
        fun confirmMobile(activationCode: String)
    }
}