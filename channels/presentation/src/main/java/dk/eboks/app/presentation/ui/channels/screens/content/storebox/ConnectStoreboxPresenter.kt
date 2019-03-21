package dk.eboks.app.presentation.ui.channels.screens.content.storebox

import androidx.annotation.VisibleForTesting
import dk.eboks.app.domain.interactors.storebox.ConfirmStoreboxInteractor
import dk.eboks.app.domain.interactors.storebox.CreateStoreboxInteractor
import dk.eboks.app.domain.interactors.storebox.LinkStoreboxInteractor
import dk.eboks.app.domain.models.local.ViewError
import dk.nodes.arch.presentation.base.BasePresenterImpl
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Christian on 5/14/2018.
 * @author Christian
 * @since 5/14/2018.
 */
internal class ConnectStoreboxPresenter @Inject constructor(
    private val linkStoreboxInteractor: LinkStoreboxInteractor,
    private val confirmStoreboxInteractor: ConfirmStoreboxInteractor,
    private val createStoreboxInteractor: CreateStoreboxInteractor
) : ConnectStoreboxContract.Presenter, BasePresenterImpl<ConnectStoreboxContract.View>(),
    LinkStoreboxInteractor.Output,
    ConfirmStoreboxInteractor.Output,
    CreateStoreboxInteractor.Output {
    @VisibleForTesting var returnCode: String? = null

    init {
        linkStoreboxInteractor.output = this
        confirmStoreboxInteractor.output = this
        createStoreboxInteractor.output = this
    }

    /**
     * Methods called by the view
     */
    override fun signIn(email: String, password: String) {
        view { showProgress(true) }
        linkStoreboxInteractor.input = LinkStoreboxInteractor.Input(email, password)
        linkStoreboxInteractor.run()
    }

    override fun confirm(code: String) {
        Timber.d("id: $returnCode code: $code")
        confirmStoreboxInteractor.input =
            ConfirmStoreboxInteractor.Input(returnCode ?: return, code)
        view { showProgress(true) }
        confirmStoreboxInteractor.run()
    }

    override fun createStoreboxUser() {
        view { showProgress(true) }
        createStoreboxInteractor.run()
    }

    /**
     * Interactor callbacks ------------------------------------------------------------------------
     */

    override fun storeboxAccountFound(found: Boolean, returnCode: String?) {
        Timber.d("storeboxAccountFound: $found")
        this.returnCode = returnCode
        view {
            showProgress(false)
            if (found) {
                showFound()
            } else {
                showNotFound()
            }
        }
    }

    override fun onLinkingSuccess(result: Boolean) {
        Timber.d("onLinkingSuccess: $result")
        view {
            if (result) {
                showSuccess()
            } else {
                showProgress(false)
                showWrongCode()
            }
        }
    }

    override fun onError(error: ViewError) {
        Timber.d("onError")
        view {
            showProgress(false)
            showErrorDialog(error)
        }
    }

    override fun onStoreboxAccountCreated() {
        view { finish() }
    }

    // this shouldnt be able to happen since we already tried to login to it :D
    override fun onStoreboxAccountExists() {
        view {
            showProgress(false)
            showErrorDialog(ViewError())
        }
    }

    override fun onStoreboxAccountCreatedError(error: ViewError) {
        view {
            showProgress(false)
            showErrorDialog(error)
        }
    }
}