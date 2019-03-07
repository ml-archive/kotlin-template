package dk.nodes.template.presentation.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dk.nodes.template.domain.interactors.PostsInteractor
import dk.nodes.template.domain.models.Result
import dk.nodes.template.domain.models.Translation
import dk.nodes.template.presentation.base.BaseViewModel
import dk.nodes.template.presentation.base.scope
import dk.nodes.template.util.Event
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
        val result = withContext(Dispatchers.IO) { postsInteractor.run() }
        when (result) {
            is Result.Success -> _viewState.value = _viewState.value?.copy(
                isLoading = false,
                posts = result.data
            )
            is Result.Error -> _viewState.value = _viewState.value?.copy(
                isLoading = false,
                errorMessage = Event(Translation.error.unknownError)
            )
        }
    }
}