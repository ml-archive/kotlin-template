package dk.nodes.template.domain.interactors

import dk.nodes.template.models.Post
import dk.nodes.template.repositories.PostRepository
import dk.nodes.template.repositories.RepositoryException
import kotlinx.coroutines.channels.Channel
import javax.inject.Inject

class PostsInteractor @Inject constructor(
    private val postRepository: PostRepository
) : BaseAsyncInteractor<List<Post>> {

    private val channel = Channel<List<Post>>()
    override suspend fun invoke(): IResult<List<Post>> {
        return try {
            val posts = postRepository.getPosts(true)
            channel.offer(posts)
            IResult.Success(posts)
        } catch (e: RepositoryException) {
            IResult.Error(e)
        }
    }

    override suspend fun receive() = channel
}
