package dk.eboks.app.presentation.ui.components.folder.folders.selectuser

import dk.eboks.app.domain.models.login.User
import dk.nodes.arch.presentation.base.BasePresenter
import dk.eboks.app.presentation.base.BaseView

/**
 * Created by bison on 07-11-2017.
 */
interface FolderSelectUserComponentContract {
    interface View : BaseView {
        fun setUser(user: User?)
    }

    interface Presenter : BasePresenter<View> {
    }
}