package dk.eboks.app.presentation.ui.screens.channels.content.storebox

import dk.eboks.app.domain.interactors.storebox.ConfirmStoreboxInteractor
import dk.eboks.app.domain.interactors.storebox.CreateStoreboxInteractor
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
                               val confirmStoreboxInteractor: ConfirmStoreboxInteractor,
                               val createStoreboxInteractor: CreateStoreboxInteractor
) : ConnectStoreboxContract.Presenter, BasePresenterImpl<ConnectStoreboxContract.View>(),
        LinkStoreboxInteractor.Output,
        ConfirmStoreboxInteractor.Output,
        CreateStoreboxInteractor.Output
{

    init {
        linkStoreboxInteractor.output = this
        confirmStoreboxInteractor.output = this
        createStoreboxInteractor.output = this
    }

    /**
     * Methods called by the view
     */
    override fun signIn(email: String, password: String) {
        runAction { v->v.showProgress(true) }
        linkStoreboxInteractor.input = LinkStoreboxInteractor.Input(email, password)
        linkStoreboxInteractor.run()
    }

    override fun confirm(code: String) {
        runAction { v->v.showProgress(true) }
        Timber.d("confirm: $code")
        confirmStoreboxInteractor.input = ConfirmStoreboxInteractor.Input("id", code) // TODO: what's id???
        confirmStoreboxInteractor.run()
    }

    override fun createStoreboxUser() {
        runAction { v->v.showProgress(true) }
        createStoreboxInteractor.run()
    }
    /**
     * Interactor callbacks ------------------------------------------------------------------------
     */

    override fun storeboxAccountFound(found: Boolean) {
        Timber.d("storeboxAccountFound: $found")
        runAction { v ->
            v.showProgress(false)
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
                v.showProgress(false)
                v.showWrongCode()
            }
        }
    }

    override fun onError(error: ViewError) {
        Timber.d("onError")
        runAction {  v ->
            v.showProgress(false)
            v.showErrorDialog(error)
        }
    }

    override fun onStoreboxAccountCreated() {
        runAction { v->v.finish() }
    }

    // this shouldnt be able to happen since we already tried to login to it :D
    override fun onStoreboxAccountExists() {
        runAction { v->
            v.showProgress(false)
            v.showErrorDialog(ViewError())
        }
    }

    override fun onStoreboxAccountCreatedError(error: ViewError) {
        runAction {  v ->
            v.showProgress(false)
            v.showErrorDialog(error)
        }
    }
}