package dk.eboks.app.presentation.ui.components.start.login.providers.idporten

import dk.eboks.app.domain.models.channel.Channel
import dk.nodes.arch.presentation.base.BasePresenter
import dk.nodes.arch.presentation.base.BaseView

/**
 * Created by bison on 07-11-2017.
 */
interface IdPortenComponentContract {
    interface View : BaseView {

    }

    interface Presenter : BasePresenter<View> {
    }
}