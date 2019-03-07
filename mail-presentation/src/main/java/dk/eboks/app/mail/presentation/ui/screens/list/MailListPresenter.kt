package dk.eboks.app.mail.presentation.ui.screens.list

import dk.eboks.app.domain.models.folder.Folder
import dk.eboks.app.domain.models.sender.Sender
import dk.nodes.arch.presentation.base.BasePresenterImpl
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
internal class MailListPresenter @Inject constructor() :
    MailListContract.Presenter,
    BasePresenterImpl<MailListContract.View>() {
    override fun setupFolder(folder: Folder) {
        view { showFolderName(folder.name) }
    }

    override fun setupSender(sender: Sender) {
        view { showFolderName(sender.name) }
    }
}