package dk.eboks.app.presentation.ui.profile.components.drawer

import dk.eboks.app.domain.models.login.LoginInfoType
import dk.eboks.app.presentation.base.BaseView
import dk.nodes.arch.presentation.base.BasePresenter

/**
 * Created by bison on 07-11-2017.
 */
interface FingerPrintComponentContract {

    interface View : BaseView {
        fun setProviderMode(mode: LoginInfoType)
    }

    interface Presenter : BasePresenter<View> {
    }
}