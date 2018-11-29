package dk.eboks.app.presentation.ui.channels.screens.content.ekey

import dk.eboks.app.domain.managers.AppStateManager
import dk.nodes.arch.presentation.base.BasePresenterImpl
import dk.nodes.locksmith.core.preferences.EncryptedPreferences

/**
 * Created by bison on 20-05-2017.
 */
class EkeyContentPresenter(val appStateManager: AppStateManager, val encryptedPreferences: EncryptedPreferences) : EkeyContentContract.Presenter, BasePresenterImpl<EkeyContentContract.View>() {
    init {
    }

    override fun getMasterKey(): String? {
        return appStateManager.state?.currentUser?.let {
            val a = encryptedPreferences.getString("ekey_${it.id}", "")
            if(a.isEmpty()) {
                null
            } else {
                a
            }
        }
    }
}