package dk.eboks.app.mail.presentation.ui.message.components.detail.folderinfo

import dk.eboks.app.presentation.base.BaseView
import dk.nodes.arch.presentation.base.BasePresenter

/**
 * Created by bison on 07-11-2017.
 */
interface FolderInfoComponentContract {
    interface View : BaseView {
        fun updateView(name: String)
    }

    interface Presenter : BasePresenter<View>
}