package dk.nodes.template.presentation.ui.main

import dk.nodes.template.domain.interactors.IResult
import dk.nodes.template.domain.interactors.PostsInteractor
import dk.nodes.template.presentation.nstack.Translation
import dk.nodes.template.presentation.ui.base.BaseViewModel
import dk.nodes.template.presentation.util.LiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MainActivityViewModel @Inject constructor(
    private val postsInteractor: PostsInteractor
) : BaseViewModel<MainActivityViewState>() {
    override fun initViewState(): MainActivityViewState {
        return MainActivityViewState()
    }

    fun fetchPosts() {
        state = state.copy(isLoading = true)
        scope.launch {
            val result = withContext(Dispatchers.IO) { postsInteractor() }
            state = when (result) {
                is IResult.Success -> state.copy(
                    isLoading = false,
                    posts = result.data
                )
                is IResult.Error -> state.copy(
                    isLoading = false,
                    errorMessage = LiveEvent(Translation.error.unknownError)
                )
            }
        }
    }
}