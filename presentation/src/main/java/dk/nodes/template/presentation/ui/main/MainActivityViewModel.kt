package dk.nodes.template.presentation.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dk.nodes.template.domain.interactors.IResult
import dk.nodes.template.domain.interactors.PostsInteractor
import dk.nodes.template.presentation.nstack.Translation
import dk.nodes.template.presentation.ui.base.BaseViewModel
import dk.nodes.template.presentation.util.LiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MainActivityViewModel @Inject constructor(
    private val postsInteractor: PostsInteractor
) : BaseViewModel() {

    private val _viewState = MutableLiveData<MainActivityViewState>()
    val viewState: LiveData<MainActivityViewState> = _viewState

    init {
        scope.launch {
            postsInteractor.receive().consumeEach {
                _viewState.value = _viewState.value?.copy(
                    isLoading = false,
                    posts = it
                )
            }
        }
    }

    fun fetchPosts() = scope.launch {
        _viewState.value = MainActivityViewState(isLoading = true)
        val result = withContext(Dispatchers.IO) { postsInteractor() }
        when (result) {
            is IResult.Error -> _viewState.value = _viewState.value?.copy(
                isLoading = false,
                errorMessage = LiveEvent(Translation.error.unknownError)
            )
        }
    }
}