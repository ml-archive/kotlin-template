package dk.eboks.app.presentation.ui.folder.components.selectuser

import android.os.Handler
import dk.eboks.app.domain.interactors.shares.GetAllSharesInteractor
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.login.SharedUser
import dk.nodes.arch.presentation.base.BasePresenterImpl
import timber.log.Timber
import java.util.*
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class FolderSelectUserComponentPresenter @Inject constructor(val appState: AppStateManager, val getAllSharesInteractor: GetAllSharesInteractor) : FolderSelectUserComponentContract.Presenter, BasePresenterImpl<FolderSelectUserComponentContract.View>(), GetAllSharesInteractor.Output {

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
            val now = Date()
            view.showShares(shares.filter { s -> s.expiryDate?.after(now) ?: false && s.status?.equals("accepted", true) ?: false })
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
        appState.state?.impersoniateUser = if(appState.state?.currentUser?.id != sharedUser?.id) {
            sharedUser
        } else {
            null
        }
    }

}