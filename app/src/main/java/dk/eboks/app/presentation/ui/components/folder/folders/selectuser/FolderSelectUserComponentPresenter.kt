package dk.eboks.app.presentation.ui.components.folder.folders.selectuser

import dk.eboks.app.domain.managers.AppStateManager
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

}