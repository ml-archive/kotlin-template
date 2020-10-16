package dk.nodes.common.usecases.posts

import dk.nodes.template.core.entities.Post
import dk.nodes.template.core.interfaces.repositories.PostRepository
import javax.inject.Inject

class GetPostUseCase @Inject constructor(
    private val postRepository: PostRepository
) {

    suspend operator fun invoke(): List<Post> {
        return postRepository.getPosts()
    }
}