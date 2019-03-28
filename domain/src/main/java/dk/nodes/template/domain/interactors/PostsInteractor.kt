package dk.nodes.template.domain.interactors

import dk.nodes.template.models.Post
import dk.nodes.template.models.Result
import dk.nodes.template.repositories.PostRepository
import dk.nodes.template.repositories.RepositoryException
import javax.inject.Inject

class PostsInteractor @Inject constructor(
    private val postRepository: PostRepository
) : BaseAsyncInteractor<Result<List<Post>>> {

    override suspend fun run(): Result<List<Post>> {
        return try {
            val posts = postRepository.getPosts(true)
            Result.Success(posts)
        } catch (e: RepositoryException) {
            Result.Error(e)
        }
    }
}