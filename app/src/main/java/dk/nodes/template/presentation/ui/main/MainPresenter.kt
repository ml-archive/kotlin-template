package dk.nodes.template.presentation.ui.main

import dk.nodes.arch.presentation.base.BasePresenterImpl
import dk.nodes.template.domain.interactors.GetPostsInteractor
import dk.nodes.template.domain.models.Post

/**
 * Created by bison on 20-05-2017.
 */
class MainPresenter(val getPostsInteractor: GetPostsInteractor) : MainContract.Presenter, BasePresenterImpl<MainContract.View>(), GetPostsInteractor.Output {
    init {
        getPostsInteractor.output = this
        run({
            getPostsInteractor.run()
        })
    }

    // implementation of the interactors callback interface
    override fun onPostsLoaded(posts: List<Post>) {
        run({ view?.showPosts(posts) })
    }

    override fun onError(msg: String) {
        run({ view?.showError(msg) })
    }
}