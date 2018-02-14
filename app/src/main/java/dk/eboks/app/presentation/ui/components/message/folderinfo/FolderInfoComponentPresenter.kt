package dk.eboks.app.presentation.ui.components.message.folderinfo

import dk.eboks.app.domain.managers.AppStateManager
import dk.nodes.arch.presentation.base.BasePresenterImpl
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class FolderInfoComponentPresenter @Inject constructor(val appState: AppStateManager) : FolderInfoComponentContract.Presenter, BasePresenterImpl<FolderInfoComponentContract.View>() {

    init {
        runAction { v->
            appState.state?.currentFolder?.let { v.updateView(it) }
        }
    }

}