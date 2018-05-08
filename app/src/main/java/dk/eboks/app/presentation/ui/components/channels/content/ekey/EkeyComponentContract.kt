package dk.eboks.app.presentation.ui.components.channels.content.ekey

import dk.eboks.app.domain.models.channel.ekey.Ekey
import dk.eboks.app.presentation.base.BaseView
import dk.nodes.arch.presentation.base.BasePresenter

/**
 * Created by bison on 07-11-2017.
 */
interface EkeyComponentContract {
    interface View : BaseView {
        fun showKeys(keys: List<Ekey>)
    }

    interface Presenter : BasePresenter<View> {
        fun getKeys()
    }
}