package dk.eboks.app.presentation.ui.channels.components.content.ekey

import dk.eboks.app.domain.models.channel.ekey.BaseEkey
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.presentation.base.BaseView
import dk.nodes.arch.presentation.base.BasePresenter

/**
 * Created by bison on 07-11-2017.
 */
interface EkeyComponentContract {
    interface View : BaseView {
    }

    interface Presenter : BasePresenter<View> {
    }
}