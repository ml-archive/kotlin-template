package dk.eboks.app.presentation.ui.main

import dk.nodes.arch.presentation.base.BasePresenter
import dk.nodes.arch.presentation.base.BaseView

/**
 * Created by bison on 07-11-2017.
 */
interface MainContract {
    interface View : BaseView {
        fun showPosts(posts : List<dk.eboks.app.domain.models.Post>)
        fun showError(msg : String)
    }

    interface Presenter : BasePresenter<dk.eboks.app.presentation.ui.main.MainContract.View> {
    }
}