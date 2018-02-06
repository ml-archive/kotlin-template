package dk.eboks.app.presentation.ui.mail.overview

import dk.eboks.app.domain.models.Folder
import dk.eboks.app.domain.models.Sender
import dk.nodes.arch.presentation.base.BasePresenter
import dk.nodes.arch.presentation.base.BaseView

/**
 * Created by bison on 07-11-2017.
 */
interface MailOverviewContract {
    interface View : BaseView {
        fun showError(msg : String)
        fun showSenders(senders : List<Sender>)
        fun showFolders(folders : List<Folder>)
        fun showRefreshProgress(show : Boolean)
        fun openFolder(folder: Folder)
    }

    interface Presenter : BasePresenter<View> {
        fun refresh()
    }
}