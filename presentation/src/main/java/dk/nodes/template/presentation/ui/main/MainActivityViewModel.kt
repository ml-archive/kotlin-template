package dk.nodes.template.presentation.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Transformations
import dk.nodes.template.domain.interactors.Fail
import dk.nodes.template.domain.interactors.Loading
import dk.nodes.template.domain.interactors.PostsInteractor
import dk.nodes.template.domain.interactors.Success
import dk.nodes.template.domain.interactors.Uninitialized
import dk.nodes.template.presentation.extensions.asChannel
import dk.nodes.template.presentation.extensions.asLiveData
import dk.nodes.template.presentation.extensions.asResult
import dk.nodes.template.presentation.extensions.asRx
import dk.nodes.template.presentation.nstack.Translation
import dk.nodes.template.presentation.ui.base.BaseViewModel
import dk.nodes.template.presentation.util.SingleEvent
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MainActivityViewModel @Inject constructor(
    postsInteractor: PostsInteractor
) : BaseViewModel() {

    private val liveDataInteractor = postsInteractor.asLiveData()
    private val resultInteractor = postsInteractor.asResult()
    private val channelInteractor = postsInteractor.asChannel()
    private val rxInteractor = postsInteractor.asRx()
    private val _viewState = MediatorLiveData<MainActivityViewState>()
    val viewState: LiveData<MainActivityViewState> = _viewState

    init {
        _viewState.addSource(Transformations.map(this.liveDataInteractor.liveData) {
            when (it) {
                is Success -> _viewState.value?.copy(posts = it.data, isLoading = false)
                is Loading -> _viewState.value?.copy(isLoading = true)
                is Fail -> _viewState.value?.copy(
                    errorMessage = SingleEvent(Translation.error.unknownError), isLoading = false
                )
                is Uninitialized -> MainActivityViewState()
            }
        }, _viewState::postValue)

        scope.launch {
            channelInteractor.receiveChannel.consumeEach {
                _viewState.postValue(
                    when (it) {
                        is Success -> _viewState.value?.copy(posts = it.data, isLoading = false)
                        is Loading -> viewState.value?.copy(isLoading = true)
                        is Fail -> viewState.value?.copy(
                            errorMessage = SingleEvent(Translation.error.unknownError),
                            isLoading = false
                        )
                        is Uninitialized -> MainActivityViewState()
                    }
                )
            }
        }

        disposables += rxInteractor.flowable
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe({
                _viewState.postValue(
                    when (it) {
                        is Success -> _viewState.value?.copy(posts = it.data, isLoading = false)
                        is Loading -> viewState.value?.copy(isLoading = true)
                        is Fail -> viewState.value?.copy(
                            errorMessage = SingleEvent(Translation.error.unknownError),
                            isLoading = false
                        )
                        is Uninitialized -> MainActivityViewState()
                    }
                )
            }, {
                _viewState.postValue(
                    viewState.value?.copy(
                        errorMessage = SingleEvent(Translation.error.unknownError),
                        isLoading = false
                    )
                )
            })
    }

    fun fetchPosts() = scope.launch(Dispatchers.IO) { rxInteractor() }

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