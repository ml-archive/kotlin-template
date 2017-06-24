package dk.nodes.template.presentation.ui.main

import dk.nodes.template.domain.models.Post

/**
 * Created by bison on 20-05-2017.
 */
interface MainMvpView : dk.nodes.template.presentation.base.MvpView {
    fun showPosts(posts : List<Post>)
    fun showError(msg : String)
}