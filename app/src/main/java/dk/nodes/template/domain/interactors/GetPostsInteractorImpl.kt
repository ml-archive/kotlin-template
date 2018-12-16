package dk.nodes.template.domain.interactors

import dk.nodes.arch.domain.interactor.Result
import dk.nodes.template.domain.models.Post
import dk.nodes.template.domain.repositories.PostRepository
import dk.nodes.template.domain.repositories.RepositoryException
import kotlinx.coroutines.CoroutineDispatcher

class GetPostsInteractorImpl(
    private val postRepository: PostRepository,
    override val dispatcher: CoroutineDispatcher
) : GetPostsInteractor {
    override suspend fun invoke(
        executeParams: GetPostsInteractor.Input,
        onResult: (Result<List<Post>>) -> Unit
    ) {
        try {
            val posts = postRepository.getPosts(true)
            onResult.invoke(Result.Success(posts))
        } catch (e: RepositoryException) {
            onResult.invoke(Result.Failure(e))
        }
    }
}