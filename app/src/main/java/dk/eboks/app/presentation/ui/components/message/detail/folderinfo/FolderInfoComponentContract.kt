package dk.eboks.app.presentation.ui.components.message.detail.folderinfo

import dk.eboks.app.domain.models.folder.Folder
import dk.nodes.arch.presentation.base.BasePresenter
import dk.nodes.arch.presentation.base.BaseView

/**
 * Created by bison on 07-11-2017.
 */
interface FolderInfoComponentContract {
    interface View : BaseView {
        fun updateView(folder : Folder)
    }

    interface Presenter : BasePresenter<FolderInfoComponentContract.View> {
    }
}