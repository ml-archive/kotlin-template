package dk.eboks.app.presentation.ui.mail.screens.list

import dk.eboks.app.domain.models.folder.Folder
import dk.eboks.app.domain.models.sender.Sender
import dk.eboks.app.mail.presentation.ui.screens.list.MailListContract
import dk.nodes.arch.presentation.base.BasePresenterImpl
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class MailListPresenter @Inject constructor() :
    MailListContract.Presenter,
    BasePresenterImpl<MailListContract.View>() {
    override fun setupFolder(folder: Folder) {
        runAction { v -> v.showFolderName(folder.name) }
    }

    override fun setupSender(sender: Sender) {
        runAction { v -> v.showFolderName(sender.name) }
    }
}