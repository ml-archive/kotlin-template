package dk.nodes.template.presentation.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Transformations
import dk.nodes.template.domain.interactors.Fail
import dk.nodes.template.domain.interactors.Loading
import dk.nodes.template.domain.interactors.PostsInteractor
import dk.nodes.template.domain.interactors.Success
import dk.nodes.template.domain.interactors.Uninitialized
import dk.nodes.template.presentation.extensions.asLiveData
import dk.nodes.template.presentation.extensions.asResult
import dk.nodes.template.presentation.nstack.Translation
import dk.nodes.template.presentation.ui.base.BaseViewModel
import dk.nodes.template.presentation.util.SingleEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MainActivityViewModel @Inject constructor(
    postsInteractor: PostsInteractor
) : BaseViewModel() {

    private val liveDataInteractor = postsInteractor.asLiveData()
    private val resultInteractor = postsInteractor.asResult()
    private val _viewState = MediatorLiveData<MainActivityViewState>()
    val viewState: LiveData<MainActivityViewState> = _viewState

    init {
        _viewState.addSource(Transformations.map(this.liveDataInteractor.liveData) {
            when (it) {
                is Success -> MainActivityViewState(posts = it.data)
                is Loading -> MainActivityViewState(isLoading = true)
                is Fail -> MainActivityViewState(
                    errorMessage = SingleEvent(Translation.error.unknownError)
                )
                is Uninitialized -> MainActivityViewState()

            }
        }, _viewState::postValue)
    }

    fun fetchPosts() = scope.launch(Dispatchers.IO) { liveDataInteractor() }

    fun fetchPostsResult() = scope.launch {
        _viewState.value = MainActivityViewState(isLoading = true)
        when (val result = withContext(Dispatchers.IO) { resultInteractor() }) {
            is Success -> _viewState.value = _viewState.value?.copy(
                isLoading = false,
                posts = result.data
            )
            is Fail -> _viewState.value = _viewState.value?.copy(
                isLoading = false,
                errorMessage = SingleEvent(Translation.error.unknownError)
            )
        }

    }
}