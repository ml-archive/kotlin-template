package dk.eboks.app.presentation.ui.components.start.login.providers.nemid

import dk.eboks.app.domain.models.login.User
import dk.nodes.arch.presentation.base.BasePresenter
import dk.nodes.arch.presentation.base.BaseView

/**
 * Created by bison on 07-11-2017.
 */
interface NemIdComponentContract {
    interface View : BaseView {
        fun setupLogin(user : User)
        fun close()
        fun proceed()
    }

    interface Presenter : BasePresenter<View> {
        fun setup()
        fun login(user : User)
        fun cancelAndClose()
    }
}