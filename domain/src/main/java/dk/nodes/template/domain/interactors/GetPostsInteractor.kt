package dk.nodes.template.domain.interactors

import dk.nodes.template.domain.interactors.base.BaseInteractor
import dk.nodes.template.models.Post
import dk.nodes.template.repositories.PostRepository
import dk.nodes.template.repositories.RepositoryException
import javax.inject.Inject

class GetPostsInteractor @Inject constructor(
    private val postRepository: PostRepository
) : BaseInteractor<List<Post>> {

    override suspend fun invoke(): InteractorResult<List<Post>> {
        return try {
            val posts = postRepository.getPosts(true)
            success(posts)
        } catch (e: RepositoryException) {
            error(e)
        }
    }
}