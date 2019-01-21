package dk.nodes.template.presentation.ui.main

import androidx.lifecycle.Lifecycle
import dk.nodes.arch.presentation.base.BasePresenterImpl
import dk.nodes.template.domain.interactors.PostsInteractor
import dk.nodes.template.domain.models.Post
import dk.nodes.template.domain.models.Result
import dk.nodes.template.domain.models.Translation
import kotlinx.coroutines.*
import javax.inject.Inject

class MainPresenter @Inject constructor(
        private val postsInteractor: PostsInteractor
) : MainContract.Presenter, BasePresenterImpl<MainContract.View>() {

    override fun onViewCreated(view: MainContract.View, lifecycle: Lifecycle) {
        super.onViewCreated(view, lifecycle)

        loadData()
    }

    private fun loadData() = GlobalScope.launch(Dispatchers.Main) {
        val result = withContext(Dispatchers.IO) { postsInteractor.run() }
        when(result) {
            is Result.Success -> view?.showPosts(result.data)
            is Result.Error -> view?.showError(Translation.error.unknownError)
        }
    }
}