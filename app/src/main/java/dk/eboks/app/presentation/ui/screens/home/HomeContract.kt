package dk.eboks.app.presentation.ui.screens.home

import dk.eboks.app.domain.models.folder.Folder
import dk.nodes.arch.presentation.base.BasePresenter
import dk.eboks.app.presentation.base.BaseView

/**
 * Created by bison on 07-11-2017.
 */
interface HomeContract {
    interface View : BaseView {
        fun addFolderPreviewComponentFragment(folder : Folder)
        fun showMailsHeader(show : Boolean)
        fun showChannelControlsHeader(show : Boolean)
    }

    interface Presenter : BasePresenter<View> {
        fun setup()
        fun refresh()
    }
}