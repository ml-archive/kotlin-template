package dk.eboks.app.presentation.ui.screens.channels.content.storebox

import dk.eboks.app.domain.interactors.storebox.ConfirmStoreboxInteractor
import dk.eboks.app.domain.interactors.storebox.LinkStoreboxInteractor
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.local.ViewError
import dk.nodes.arch.presentation.base.BasePresenterImpl
import timber.log.Timber

/**
 * Created by Christian on 5/14/2018.
 * @author   Christian
 * @since    5/14/2018.
 */
class ConnectStoreboxPresenter(val appStateManager: AppStateManager,
                               val linkStoreboxInteractor: LinkStoreboxInteractor,
                               val confirmStoreboxInteractor: ConfirmStoreboxInteractor
) : ConnectStoreboxContract.Presenter, BasePresenterImpl<ConnectStoreboxContract.View>(),
        LinkStoreboxInteractor.Output,
        ConfirmStoreboxInteractor.Output {

    init {
        linkStoreboxInteractor.output = this
        confirmStoreboxInteractor.output = this
    }

    override fun storeboxAccountFound(found: Boolean) {
        Timber.d("storeboxAccountFound: $found")
        runAction { v ->
            if(found) {
                v.showFound()
            }
            else {
                v.showNotFound()
            }
        }
    }

    override fun onLinkingSuccess(result: Boolean) {
        Timber.d("onLinkingSuccess: $result")
        runAction { v ->
            if(result) {
                v.showSuccess()
            } else {
                v.showWrongCode()
            }
        }
    }

    override fun onError(error: ViewError) {
        Timber.d("onError")
        runAction {  v ->
            v.showErrorDialog(error)
        }
    }

    override fun signIn(email: String, password: String) {
        Timber.d("signIn: $email, $password")
        linkStoreboxInteractor.input = LinkStoreboxInteractor.Input(email, password)
        linkStoreboxInteractor.run()
    }

    override fun confirm(code: String) {
        Timber.d("confirm: $code")
        confirmStoreboxInteractor.input = ConfirmStoreboxInteractor.Input("id", code) // TODO: what's id???
        confirmStoreboxInteractor.run()
    }

}