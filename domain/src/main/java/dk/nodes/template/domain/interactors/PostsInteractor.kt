package dk.nodes.template.domain.interactors

import dk.nodes.template.models.Post
import dk.nodes.template.repositories.PostRepository
import dk.nodes.template.repositories.RepositoryException
import javax.inject.Inject

class PostsInteractor @Inject constructor(
    private val postRepository: PostRepository
) : BaseAsyncInteractor<InteractorResult<List<Post>>> {

    override suspend fun invoke(): InteractorResult<List<Post>> {
        return try {
            val posts = postRepository.getPosts(true)
            InteractorResult.Success(posts)
        } catch (e: RepositoryException) {
            InteractorResult.Error(e)
        }
    }
}