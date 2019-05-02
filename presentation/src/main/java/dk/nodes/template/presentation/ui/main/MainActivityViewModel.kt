package dk.nodes.template.presentation.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Transformations
import dk.nodes.template.domain.interactors.Fail
import dk.nodes.template.domain.interactors.InteractorResult
import dk.nodes.template.domain.interactors.Loading
import dk.nodes.template.domain.interactors.PostsInteractor
import dk.nodes.template.domain.interactors.Success
import dk.nodes.template.domain.interactors.Uninitialized
import dk.nodes.template.models.Post
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
import kotlinx.coroutines.channels.map
import kotlinx.coroutines.launch
import timber.log.Timber
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
        _viewState.addSource(
            Transformations.map(this.liveDataInteractor.liveData, this::mapResult),
            _viewState::postValue
        )

        scope.launch {
            channelInteractor.receive()
                .map(Dispatchers.IO) { mapResult(it) }
                .consumeEach(_viewState::postValue)
        }

        disposables += rxInteractor.observe()
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .map(this::mapResult)
            .onErrorReturn { mapResult(Fail(it)) }
            .subscribe(_viewState::postValue, Timber::e)
    }

    fun fetchPosts() = scope.launch(Dispatchers.IO) {
        /** Uncomment below to test RxInteractor */
//         rxInteractor()

        /** Uncomment below to test ChannelInteractor */
//         channelInteractor()

        /** Uncomment below to test LiveDataInteractor */
//         liveDataInteractor()

        /** Uncomment below to test ResultInteractor */
//        _viewState.postValue(mapResult(Loading()))
//        _viewState.postValue(mapResult(resultInteractor()))
    }

    private fun mapResult(result: InteractorResult<List<Post>>): MainActivityViewState {
        return when (result) {
            is Success -> _viewState.value?.copy(posts = result.data, isLoading = false)
                ?: MainActivityViewState(posts = result.data)
            is Loading -> viewState.value?.copy(isLoading = true)
                ?: MainActivityViewState(isLoading = true)
            is Fail -> viewState.value?.copy(
                errorMessage = SingleEvent(Translation.error.unknownError),
                isLoading = false
            ) ?: MainActivityViewState(errorMessage = SingleEvent(Translation.error.unknownError))
            is Uninitialized -> MainActivityViewState()
        }
    }
}