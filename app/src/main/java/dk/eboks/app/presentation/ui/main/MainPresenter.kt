package dk.eboks.app.presentation.ui.main

import dk.nodes.arch.presentation.base.BasePresenterImpl

/**
 * Created by bison on 20-05-2017.
 */
class MainPresenter(val getPostsInteractor: dk.eboks.app.domain.interactors.GetPostsInteractor) : dk.eboks.app.presentation.ui.main.MainContract.Presenter, BasePresenterImpl<dk.eboks.app.presentation.ui.main.MainContract.View>(), dk.eboks.app.domain.interactors.GetPostsInteractor.Output {
    init {
        getPostsInteractor.output = this
        runAction {
            getPostsInteractor.run()
        }
    }

    // implementation of the interactors callback interface
    override fun onPostsLoaded(posts: List<dk.eboks.app.domain.models.Post>) {
        runAction{ view?.showPosts(posts) }
    }

    override fun onError(msg: String) {
        runAction{ view?.showError(msg) }
    }
}