package dk.eboks.app.presentation.ui.folder.components.selectuser

import android.os.Handler
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.login.SharedUser
import dk.nodes.arch.presentation.base.BasePresenterImpl
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class FolderSelectUserComponentPresenter @Inject constructor(val appState: AppStateManager) : FolderSelectUserComponentContract.Presenter, BasePresenterImpl<FolderSelectUserComponentContract.View>() {

    init {
        runAction { v ->
            v.setUser(appState.state?.currentUser)
        }
    }

    override fun getShared() {
        Handler().postDelayed({
            val mocks = createMocks()
            runAction { view ->
                view.showShares(mocks)
                view.showProgress(false)
            }
        }, 2000)
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