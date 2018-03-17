package dk.eboks.app.presentation.ui.components.start.login.providers.bankidno

import dk.eboks.app.domain.models.channel.Channel
import dk.nodes.arch.presentation.base.BasePresenter
import dk.nodes.arch.presentation.base.BaseView

/**
 * Created by bison on 07-11-2017.
 */
interface BankIdNOComponentContract {
    interface View : BaseView {

    }

    interface Presenter : BasePresenter<View> {
    }
}