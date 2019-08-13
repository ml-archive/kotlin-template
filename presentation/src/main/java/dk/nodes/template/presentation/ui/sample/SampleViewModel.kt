package dk.nodes.template.presentation.ui.sample

import dk.nodes.template.domain.interactors.PostsInteractor
import dk.nodes.template.presentation.extensions.asResult
import dk.nodes.template.presentation.extensions.isError
import dk.nodes.template.presentation.extensions.isSuccess
import dk.nodes.template.presentation.ui.base.BaseViewModel
import dk.nodes.template.presentation.ui.base.Reducer
import dk.nodes.template.presentation.util.SingleEvent
import dk.nodes.template.presentation.util.ViewErrorController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class SampleViewModel @Inject constructor(
        postsInteractor: PostsInteractor
) : BaseViewModel<Action, Change, SampleViewState>() {

    override val initState: SampleViewState = SampleViewState()

    private val resultInteractor = postsInteractor.asResult()


    override val reducer: Reducer<SampleViewState, Change> = { state, change ->
        when (change) {
            is Change.PostsLoading -> state.copy(isLoading = true)
            is Change.PostsLoadingError -> state.copy(
                    isLoading = false,
                    viewError = SingleEvent(ViewErrorController.mapThrowable(change.throwable))
            )
            is Change.PostsLoaded -> state.copy(posts = change.posts, isLoading = false)
        }
    }

    override fun emitAction(action: Action): Flow<Change> {
        return when (action) {
            is Action.LoadPosts -> bindLoadPostsAction().flowOn(Dispatchers.IO)
        }
    }

    private fun bindLoadPostsAction(): Flow<Change> = flow {
        emit(Change.PostsLoading)
        resultInteractor()
                .isError { emit(Change.PostsLoadingError(it)) }
                .isSuccess { emit(Change.PostsLoaded(it)) }
    }


}