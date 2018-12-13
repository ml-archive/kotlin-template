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
        fun showKeys(keys: List<BaseEkey>)
        fun onGetMasterkeyError(viewError: ViewError)
        fun showPinView()
    }

    interface Presenter : BasePresenter<View> {
        fun getKeys()
        fun getMasterkeyFromBackend(pin: String)
        fun deleteMasterKey()
        fun storeMasterkey(masterKey: String)
        fun getVault(masterKey: String)
        fun setVault(masterKey: String, keyList: MutableList<BaseEkey>)
        fun deleteVault(signature: String, signatureTime: String)
        fun decryptVault(masterKey: String, vault: String): String
        fun getKeyList(): MutableList<BaseEkey>
        fun getMasterKeyLocally()
        var masterKey: String?
    }
}