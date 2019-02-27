package dk.eboks.app.presentation.ui.channels.screens.content.ekey

import dk.eboks.app.domain.models.channel.ekey.BaseEkey
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.presentation.base.BaseView
import dk.nodes.arch.presentation.base.BasePresenter

/**
 * Created by bison on 07-11-2017.
 */
interface EkeyContentContract {
    interface View : BaseView {
        fun showKeys(keys: ArrayList<BaseEkey>)
        fun showPinView(isCreate: Boolean)
        fun onGetMasterkeyError(viewError: ViewError)
    }

    interface Presenter : BasePresenter<View> {
        fun getData()
        var pin: String?
        fun putVault(items: ArrayList<BaseEkey>)
    }
}