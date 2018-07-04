package dk.eboks.app.presentation.ui.mail.screens.list

import dk.eboks.app.domain.models.folder.Folder
import dk.eboks.app.domain.models.sender.Sender
import dk.eboks.app.presentation.base.BaseView
import dk.nodes.arch.presentation.base.BasePresenter

/**
 * Created by bison on 07-11-2017.
 */
interface MailListContract {
    interface View : BaseView {
        fun showFolderName(name : String)
    }

    interface Presenter : BasePresenter<View> {
        fun setupFolder(folder : Folder)
        fun setupSender(sender : Sender)
    }
}