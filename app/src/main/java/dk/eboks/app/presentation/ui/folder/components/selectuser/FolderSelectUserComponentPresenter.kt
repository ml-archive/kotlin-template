package dk.eboks.app.presentation.ui.folder.components.selectuser

import dk.eboks.app.domain.interactors.shares.GetAllSharesInteractor
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.login.SharedUser
import dk.nodes.arch.presentation.base.BasePresenterImpl
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class FolderSelectUserComponentPresenter @Inject constructor(
        private val appState: AppStateManager,
        private val getAllSharesInteractor: GetAllSharesInteractor
) : FolderSelectUserComponentContract.Presenter,
    BasePresenterImpl<FolderSelectUserComponentContract.View>(),
    GetAllSharesInteractor.Output {

    init {
        runAction { v ->
            v.setUser(appState.state?.currentUser)
        }
        getAllSharesInteractor.output = this
    }

    override fun getShared() {
        getAllSharesInteractor.run()
    }

    override fun onGetAllShares(shares: List<SharedUser>) {
        runAction { view ->
            view.showShares(shares)
            view.showProgress(false)
        }
    }

    override fun onGetAllSharesError(viewError: ViewError) {
        runAction { view ->
            view.showErrorDialog(viewError)
            view.showProgress(false)
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