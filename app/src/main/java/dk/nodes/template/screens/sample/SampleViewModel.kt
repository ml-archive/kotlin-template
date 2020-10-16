package dk.nodes.template.screens.sample

import androidx.lifecycle.viewModelScope
import dk.nodes.common.usecases.Result
import dk.nodes.common.usecases.posts.GetPostUseCase
import dk.nodes.common.usecases.safeSuspendCall
import dk.nodes.template.base.BaseViewModel
import dk.nodes.template.core.entities.Post
import dk.nodes.template.util.SingleEvent
import dk.nodes.template.util.ViewErrorController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SampleViewModel @Inject constructor(
    private val getPostUseCase: GetPostUseCase
) : BaseViewModel<SampleViewState>(SampleViewState()) {

    fun fetchPosts() {
        viewModelScope.launch(Dispatchers.Main) {
            val result = withContext(Dispatchers.IO) {
                safeSuspendCall { getPostUseCase() }
            }
            state = mapResult(result)
        }
    }

    fun switchTheme() {
        viewModelScope.launch {
        }
    }

    private fun mapResult(result: Result<List<Post>>): SampleViewState {
        return when (result) {
            is Result.Success -> state.copy(isLoading = false, posts = result.data)
            is Result.Error -> state.copy(
                viewError = SingleEvent(ViewErrorController.mapThrowable(result.exception)),
                isLoading = false
            )
            else -> SampleViewState()
        }
    }
}