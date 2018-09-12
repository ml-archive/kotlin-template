package dk.nodes.template.presentation.ui.main

import dk.nodes.arch.presentation.base.BasePresenterImpl
import dk.nodes.template.domain.interactors.GetPostsInteractor
import dk.nodes.template.domain.models.Post

class MainPresenter(private val getPostsInteractor: GetPostsInteractor) : MainContract.Presenter,
    BasePresenterImpl<MainContract.View>(), GetPostsInteractor.Output {
    init {
        getPostsInteractor.output = this
        runAction {
            getPostsInteractor.run()
        }
    }

    // implementation of the interactors callback interface

    override fun onPostsLoaded(posts: List<Post>) {
        runAction { view?.showPosts(posts) }
    }

    override fun onError(msg: String) {
        runAction { view?.showError(msg) }
    }
}