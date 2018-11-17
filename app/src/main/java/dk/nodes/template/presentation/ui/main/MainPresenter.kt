package dk.nodes.template.presentation.ui.main

import android.arch.lifecycle.Lifecycle
import dk.nodes.arch.presentation.base.BasePresenterImpl
import dk.nodes.template.domain.interactors.PostsInteractor
import dk.nodes.template.domain.models.Post
import dk.nodes.template.domain.models.Translation
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import javax.inject.Inject

class MainPresenter @Inject constructor(
        private val postsInteractor: PostsInteractor
) : MainContract.Presenter, BasePresenterImpl<MainContract.View>() {

    override fun onViewCreated(view: MainContract.View, lifecycle: Lifecycle) {
        super.onViewCreated(view, lifecycle)

        loadData()
    }

    private fun loadData() = launch(UI) {
        val result = async { postsInteractor.run() }.await()
        when(result) {
            is PostsInteractor.Result.Success -> view?.showPosts(posts = result.posts)
            is PostsInteractor.Result.Failure -> view?.showError(Translation.error.unknownError)
        }
    }
}