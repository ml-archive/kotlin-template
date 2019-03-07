package dk.eboks.app.mail.presentation.ui.folder.components

import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.login.SharedUser
import dk.eboks.app.mail.domain.interactors.shares.GetAllSharesInteractor
import dk.nodes.arch.presentation.base.BasePresenterImpl
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
internal class FolderSelectUserComponentPresenter @Inject constructor(
    private val appState: AppStateManager,
    private val getAllSharesInteractor: GetAllSharesInteractor
) : FolderSelectUserComponentContract.Presenter,
    BasePresenterImpl<FolderSelectUserComponentContract.View>(),
    GetAllSharesInteractor.Output {

    init {
        view { setUser(appState.state?.currentUser) }
        getAllSharesInteractor.output = this
    }

    override fun getShared() {
        getAllSharesInteractor.run()
    }

    override fun onGetAllShares(shares: List<SharedUser>) {
        view {
            showShares(shares)
            showProgress(false)
        }
    }

    override fun onGetAllSharesError(viewError: ViewError) {
        view {
            showErrorDialog(viewError)
            showProgress(false)
        }
    }

    override fun setSharedUser(sharedUser: SharedUser?) {
        appState.state?.impersoniateUser = if (appState.state?.currentUser?.id != sharedUser?.id) {
            sharedUser
        } else {
            null
        }
    }
}