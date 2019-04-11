package dk.nodes.template.domain.interactors

import dk.nodes.template.domain.extensions.safeCall
import dk.nodes.template.models.Post
import dk.nodes.template.repositories.PostRepository
import javax.inject.Inject

class PostsInteractor @Inject constructor(
    private val postRepository: PostRepository
) : BaseAsyncInteractor<List<Post>> {

    override suspend fun invoke(): IResult<List<Post>> {
        return safeCall { postRepository.getPosts(true) }
    }
}