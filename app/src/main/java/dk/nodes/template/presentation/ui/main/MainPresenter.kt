package dk.nodes.template.presentation.ui.main

import dk.nodes.template.domain.interactors.GetPostsInteractor
import dk.nodes.template.domain.interactors.GetPostsInteractorImpl
import dk.nodes.template.domain.models.Post
import dk.nodes.template.network.ApiProxy
import dk.nodes.template.network.RestPostRepository
import dk.nodes.template.presentation.base.MvpBasePresenter

/**
 * Created by bison on 20-05-2017.
 */
class MainPresenter(val api: ApiProxy) : MvpBasePresenter<MainMvpView>(), GetPostsInteractor.Callback {
    init {
    }

    override fun attachView(view: MainMvpView) {
        super.attachView(view)
        android.util.Log.e("debug", "attachView")

        val interactor : GetPostsInteractor = GetPostsInteractorImpl(this@MainPresenter, RestPostRepository(api))
        interactor.run()
    }

    override fun detachView() {
        super.detachView()
        android.util.Log.e("debug", "detachView")
    }

    // implementation of the interactors callback interface
    override fun onPostsLoaded(posts: List<Post>) {
        if(isViewAttached)
            view?.showPosts(posts)
    }

    override fun onError(msg: String) {
        if(isViewAttached)
            view?.showError(msg)
    }
}