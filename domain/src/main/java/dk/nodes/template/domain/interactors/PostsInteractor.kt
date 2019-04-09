package dk.nodes.template.domain.interactors

import dk.nodes.template.models.Post
import dk.nodes.template.repositories.PostRepository
import dk.nodes.template.repositories.RepositoryException
import javax.inject.Inject

class PostsInteractor @Inject constructor(
    private val postRepository: PostRepository
) : BaseAsyncInteractor<IResult<List<Post>>> {

    override suspend fun invoke(): IResult<List<Post>> {
        return try {
            val posts = postRepository.getPosts(true)
            IResult.Success(posts)
        } catch (e: RepositoryException) {
            IResult.Error(e)
        }
    }
}