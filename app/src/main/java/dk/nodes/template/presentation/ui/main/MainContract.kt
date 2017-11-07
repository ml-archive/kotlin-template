package dk.nodes.template.presentation.ui.main

import dk.nodes.template.domain.models.Post
import dk.nodes.template.presentation.base.MvpPresenter
import dk.nodes.template.presentation.base.MvpView

/**
 * Created by bison on 07-11-2017.
 */
interface MainContract {
    interface View : MvpView {
        fun showPosts(posts : List<Post>)
        fun showError(msg : String)
    }

    interface Presenter : MvpPresenter<View> {
    }
}