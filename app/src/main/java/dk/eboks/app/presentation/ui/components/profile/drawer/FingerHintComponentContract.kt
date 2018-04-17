package dk.eboks.app.presentation.ui.components.profile.drawer

import dk.eboks.app.domain.models.login.LoginInfo
import dk.eboks.app.presentation.base.BaseView
import dk.nodes.arch.presentation.base.BasePresenter

/**
 * Created by bison on 07-11-2017.
 */
interface FingerHintComponentContract {
    interface View : BaseView {
        fun getUserLoginInfo(): LoginInfo
        fun finishView()
    }

    interface Presenter : BasePresenter<View> {
        fun encryptUserLoginInfo()
    }
}