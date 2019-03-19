package dk.eboks.app.mail.presentation.ui.message.components.detail.folderinfo

import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.util.guard
import dk.nodes.arch.presentation.base.BasePresenterImpl
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
internal class FolderInfoComponentPresenter @Inject constructor(private val appState: AppStateManager) :
    FolderInfoComponentContract.Presenter, BasePresenterImpl<FolderInfoComponentContract.View>() {

    init {
        view {
            appState.state?.currentMessage?.folder?.name?.let(::updateView).guard {
                appState.state?.currentFolder?.name?.let(::updateView)
            }
        }
    }
}