package dk.eboks.app.presentation.ui.screens.channels.content.storebox

import dk.eboks.app.domain.managers.AppStateManager
import dk.nodes.arch.presentation.base.BasePresenterImpl
import timber.log.Timber

/**
 * Created by Christian on 5/14/2018.
 * @author   Christian
 * @since    5/14/2018.
 */
class ConnectStoreboxPresenter(val appStateManager: AppStateManager) : ConnectStoreboxContract.Presenter, BasePresenterImpl<ConnectStoreboxContract.View>() {

    override fun signIn(email: String, password: String) {
        Timber.d("signIn: $email, $password")
    }

    override fun confirm(code: String) {
        Timber.d("confirm: $code")
    }

}