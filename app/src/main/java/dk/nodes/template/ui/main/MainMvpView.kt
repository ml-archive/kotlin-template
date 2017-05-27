package dk.nodes.template.ui.main

import dk.nodes.template.api.Post
import dk.nodes.template.mvp.MvpView

/**
 * Created by bison on 20-05-2017.
 */
interface MainMvpView : dk.nodes.template.mvp.MvpView {
    fun showPosts(posts : List<dk.nodes.template.api.Post>)
}