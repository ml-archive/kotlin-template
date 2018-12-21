package dk.eboks.app.presentation.ui.folder.components.selectuser

import android.os.Handler
import dk.eboks.app.domain.interactors.shares.GetAllSharesInteractor
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.login.SharedUser
import dk.nodes.arch.presentation.base.BasePresenterImpl
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
//        Handler().postDelayed({
//            val mocks = createMocks()
//            runAction { view ->
//                view.showShares(mocks)
//                view.showProgress(false)
//            }
//        }, 2000)
        getAllSharesInteractor.run()
    }

    override fun onGetAllShares(shares: List<SharedUser>) {
        runAction { view ->
            view.showShares(shares)
            view.showProgress(false)
        }
    }

    override fun onGetAllSharesError(viewError: ViewError) {
        runAction { view -> view.showErrorDialog(viewError) }
    }

    private fun createMocks(): MutableList<SharedUser> {
        val sharedUsers = mutableListOf<SharedUser>()
        sharedUsers.add(SharedUser(1, 2, "_*Peter Petersen", "_*Administrator", null, null))
        sharedUsers.add(SharedUser(1, 3, "_*John Johnson", "_*Read only", null, null))
        sharedUsers.add(SharedUser(1, 4, "_*Søren Sørensen", "_*Read only", null, null))
        sharedUsers.add(SharedUser(1, 5, "_*Ole Olsen", "_*Administrator", null, null))
        return sharedUsers
    }
}