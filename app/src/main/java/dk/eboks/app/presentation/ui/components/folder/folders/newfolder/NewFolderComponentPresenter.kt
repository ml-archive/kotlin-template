package dk.eboks.app.presentation.ui.components.folder.folders.newfolder

import dk.eboks.app.domain.managers.AppStateManager
import dk.nodes.arch.presentation.base.BasePresenterImpl
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class NewFolderComponentPresenter @Inject constructor(val appState: AppStateManager) : NewFolderComponentContract.Presenter, BasePresenterImpl<NewFolderComponentContract.View>() {

    init {
        appState.state?.currentUser?.let { user ->
            runAction { v ->
                v.setRootFolder(user.name)

            }
        }
    }


}