package dk.eboks.app.presentation.ui.profile.components.drawer

import dk.eboks.app.presentation.base.BaseView
import dk.nodes.arch.presentation.base.BasePresenter

/**
 * Created by bison on 07-11-2017.
 */
interface EmailVerificationComponentContract {
    interface View : BaseView {
        fun setVerifyBtnEnabled(enabled : Boolean)
        fun finishActivity(resultCode : Int?)
    }

    interface Presenter : BasePresenter<View> {
        fun verifyMail(mail:String)
    }
}