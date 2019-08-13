package dk.nodes.template.presentation.ui.sample

import androidx.lifecycle.viewModelScope
import dk.nodes.template.domain.interactors.*
import dk.nodes.template.models.Post
import dk.nodes.template.presentation.extensions.asResult
import dk.nodes.template.presentation.extensions.runInteractor
import dk.nodes.template.presentation.ui.base.BaseViewModel
import dk.nodes.template.presentation.util.SingleEvent
import dk.nodes.template.presentation.util.ViewErrorController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class SampleViewModel @Inject constructor(
    postsInteractor: PostsInteractor
) : BaseViewModel<SampleViewState>() {

    override val initState: SampleViewState = SampleViewState()

    private val resultInteractor = postsInteractor.asResult()

    fun fetchPosts() = viewModelScope.launch(Dispatchers.Main) {
        state = mapResult(Loading())
        val result = runInteractor(resultInteractor)
        state = mapResult(result)
    }

    private fun mapResult(result: InteractorResult<List<Post>>): SampleViewState {
        return when (result) {
            is Success -> state.copy(posts = result.data, isLoading = false)
            is Loading -> state.copy(isLoading = true)
            is Fail -> state.copy(
                viewError = SingleEvent(ViewErrorController.mapThrowable(result.throwable)),
                isLoading = false
            )
            else -> SampleViewState()
        }
    }
}