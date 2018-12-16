package dk.nodes.template.domain.interactors

import dk.nodes.arch.extensions.safeSuspendCall
import dk.nodes.arch.util.AppCoroutineDispatchers
import dk.nodes.template.domain.models.Post
import dk.nodes.template.domain.repositories.PostRepository
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class GetPostsInteractorImpl @Inject constructor(
    private val postRepository: PostRepository,
    dispatchers: AppCoroutineDispatchers
) : GetPostsInteractor {
    override val dispatcher: CoroutineDispatcher = dispatchers.io

    override suspend fun invoke(
        executeParams: GetPostsInteractor.Input,
        onResult: (Result<List<Post>>) -> Unit
    ) {
        safeSuspendCall(
            call = { postRepository.getPosts(true) },
            errorMessage = "Error Getting posts",
            onResult = onResult
        )
    }
}