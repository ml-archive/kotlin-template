package dk.nodes.template.domain.interactors

import dk.nodes.template.models.Post
import dk.nodes.template.repositories.PostRepository
import javax.inject.Inject

class PostsInteractor @Inject constructor(
    private val postRepository: PostRepository
) : NoInputInteractor<List<Post>> {

    override suspend fun invoke(input: Unit): List<Post> {
        return postRepository.getPosts()
    }
}