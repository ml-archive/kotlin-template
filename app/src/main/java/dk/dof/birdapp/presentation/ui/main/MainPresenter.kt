package dk.dof.birdapp.presentation.ui.main

import dk.dof.birdapp.domain.interactors.GetPostsInteractor
import dk.dof.birdapp.domain.models.Post
import dk.nodes.arch.presentation.base.BasePresenterImpl

/**
 * Created by bison on 20-05-2017.
 */
class MainPresenter(val getPostsInteractor: GetPostsInteractor) : MainContract.Presenter, BasePresenterImpl<MainContract.View>(), GetPostsInteractor.Output {
    init {
        getPostsInteractor.output = this
        runAction {
            getPostsInteractor.run()
        }
    }

    // implementation of the interactors callback interface
    override fun onPostsLoaded(posts: List<Post>) {
        runAction {
            //            view?.showPosts(posts)
        }
    }

    override fun onError(msg: String) {
        runAction {
            // view?.showError(msg)
        }
    }
}