package dk.nodes.template.presentation.ui.main

import dk.nodes.template.domain.interactors.GetPostsInteractor
import dk.nodes.template.domain.models.Post
import dk.nodes.template.presentation.base.MvpBasePresenter
import timber.log.Timber

/**
 * Created by bison on 20-05-2017.
 */
class MainPresenter(val getPostsInteractor: GetPostsInteractor) : MainContract.Presenter, MvpBasePresenter<MainContract.View>(), GetPostsInteractor.Output {
    init {
        getPostsInteractor.output = this
    }

    override fun attachView(view: MainContract.View) {
        super.attachView(view)
        Timber.d("attachView")
        getPostsInteractor.run()
    }

    override fun detachView() {
        super.detachView()
        Timber.d("detachView")
    }

    // implementation of the interactors callback interface
    override fun onPostsLoaded(posts: List<Post>) {
        view?.showPosts(posts)
    }

    override fun onError(msg: String) {
        view?.showError(msg)
    }
}