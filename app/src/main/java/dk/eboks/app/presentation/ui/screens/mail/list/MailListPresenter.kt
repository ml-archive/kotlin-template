package dk.eboks.app.presentation.ui.screens.mail.list

import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.folder.Folder
import dk.eboks.app.domain.models.folder.FolderType
import dk.eboks.app.domain.models.sender.Sender
import dk.nodes.arch.presentation.base.BasePresenterImpl
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class MailListPresenter @Inject constructor(val appState: AppStateManager) :
        MailListContract.Presenter,
        BasePresenterImpl<MailListContract.View>()
{
    override fun setupFolder(folder: Folder) {
        runAction { v->v.showFolderName(folder.name) }
    }

    override fun setupSender(sender: Sender) {
        runAction { v->v.showFolderName(sender.name) }
    }
}