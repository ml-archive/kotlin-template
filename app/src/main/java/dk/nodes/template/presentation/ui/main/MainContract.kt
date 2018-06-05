package dk.nodes.template.presentation.ui.main

import dk.nodes.arch.presentation.base.BasePresenter
import dk.nodes.arch.presentation.base.BaseView
import dk.nodes.template.domain.models.Post

/**
 * Created by bison on 07-11-2017.
 */
interface MainContract {
    interface View : BaseView {
        fun showPosts(posts: List<Post>)
        fun showError(msg: String)
    }

    interface Presenter : BasePresenter<View> {
    }
}