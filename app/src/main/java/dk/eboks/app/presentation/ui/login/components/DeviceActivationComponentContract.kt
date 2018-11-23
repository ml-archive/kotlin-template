package dk.eboks.app.presentation.ui.login.components

import dk.eboks.app.presentation.base.BaseView
import dk.nodes.arch.presentation.base.BasePresenter

/**
 * Created by bison on 07-11-2017.
 */
interface DeviceActivationComponentContract {
    interface View : BaseView {
        fun showProgress(show : Boolean)
        fun setupButtons()
        fun requestNemidLogin()
        fun closeDrawer()
        fun login()
    }

    interface Presenter : BasePresenter<View> {
        fun activateDevice()
        fun requestNemidLogin()
    }
}