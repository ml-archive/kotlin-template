package dk.nodes.template.presentation.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dk.nodes.template.domain.extensions.asResult
import dk.nodes.template.domain.interactors.InteractorResult
import dk.nodes.template.domain.interactors.PostsInteractor
import dk.nodes.template.presentation.nstack.Translation
import dk.nodes.template.presentation.ui.base.BaseViewModel
import dk.nodes.template.presentation.util.SingleEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MainActivityViewModel @Inject constructor(
    private val postsInteractor: PostsInteractor
) : BaseViewModel() {

    private val _viewState = MutableLiveData<MainActivityViewState>()
    val viewState: LiveData<MainActivityViewState> = _viewState

    fun fetchPosts() = scope.launch {
        _viewState.value = MainActivityViewState(isLoading = true)
        val result = withContext(Dispatchers.IO) { postsInteractor.asResult()() }
        when (result) {
            is InteractorResult.Success -> _viewState.value = _viewState.value?.copy(
                isLoading = false,
                posts = result.data
            )
            is InteractorResult.Error -> _viewState.value = _viewState.value?.copy(
                isLoading = false,
                errorMessage = SingleEvent(Translation.error.unknownError)
            )
        }
    }
}