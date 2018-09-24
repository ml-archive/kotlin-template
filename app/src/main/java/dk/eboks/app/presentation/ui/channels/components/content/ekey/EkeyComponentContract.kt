package dk.eboks.app.presentation.ui.channels.components.content.ekey

import dk.eboks.app.domain.models.channel.ekey.BaseEkey
import dk.eboks.app.domain.models.channel.ekey.Ekey
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.presentation.base.BaseView
import dk.nodes.arch.presentation.base.BasePresenter

/**
 * Created by bison on 07-11-2017.
 */
interface EkeyComponentContract {
    interface View : BaseView {
        fun showKeys(keys: List<BaseEkey>)
        fun onMasterkey(masterkey: String?)
        fun onGetMasterkeyError(viewError: ViewError)
    }

    interface Presenter : BasePresenter<View> {
        fun getKeys()
        fun getKeys(signatureTime: String, signature: String)
        fun getMasterkey()
        fun setMasterkey(hash: String, encrypted: String)
        fun storeMasterkey(masterKey: String)
    }
}