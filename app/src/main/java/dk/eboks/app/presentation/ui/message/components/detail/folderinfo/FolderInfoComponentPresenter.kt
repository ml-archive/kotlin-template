package dk.eboks.app.presentation.ui.message.components.detail.folderinfo

import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.util.guard
import dk.nodes.arch.presentation.base.BasePresenterImpl
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class FolderInfoComponentPresenter @Inject constructor(val appState: AppStateManager) :
    FolderInfoComponentContract.Presenter, BasePresenterImpl<FolderInfoComponentContract.View>() {

    init {
        runAction { v ->

            appState.state?.currentMessage?.folder?.let {
                v.updateView(it.name)
            }.guard {
                appState.state?.currentFolder?.let { v.updateView(it.name) }
            }
        }
    }
}