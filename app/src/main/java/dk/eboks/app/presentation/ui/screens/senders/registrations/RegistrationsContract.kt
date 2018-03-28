package dk.eboks.app.presentation.ui.screens.senders.registrations

import dk.eboks.app.domain.models.sender.Registrations
import dk.eboks.app.presentation.base.BaseView
import dk.nodes.arch.presentation.base.BasePresenter

/**
 * Created by Christian on 3/28/2018.
 * @author   Christian
 * @since    3/28/2018.
 */
interface RegistrationsContract {
    interface View : BaseView {
        fun showRegistrations(registrations: Registrations)
    }

    interface Presenter : BasePresenter<View> {
    }
}