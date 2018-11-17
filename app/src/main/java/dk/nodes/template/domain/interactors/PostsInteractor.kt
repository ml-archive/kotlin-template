package dk.nodes.template.domain.interactors

import dk.nodes.template.domain.models.Post
import dk.nodes.template.domain.repositories.PostRepository
import dk.nodes.template.domain.repositories.RepositoryException
import javax.inject.Inject

class PostsInteractor @Inject constructor(
    private val postRepository: PostRepository
): BaseAsyncInteractor<PostsInteractor.Result> {

    sealed class Result {
        data class Success(val posts: List<Post>): Result()
        object Failure: Result()
    }

    override suspend fun run(): Result {
        return try {
            val posts = postRepository.getPosts(true)
            Result.Success(posts)
        } catch (e: RepositoryException) {
            Result.Failure
        }
    }
}