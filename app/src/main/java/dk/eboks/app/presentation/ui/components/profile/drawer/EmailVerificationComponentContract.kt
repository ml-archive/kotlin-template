package dk.eboks.app.presentation.ui.components.profile.drawer

import dk.eboks.app.presentation.base.BaseView
import dk.nodes.arch.presentation.base.BasePresenter

/**
 * Created by bison on 07-11-2017.
 */
interface EmailVerificationComponentContract {
    interface View : BaseView {
        fun finishActivity(resultCode : Int?)
    }

    interface Presenter : BasePresenter<View> {
        fun verifyMail(mail:String)
    }
}