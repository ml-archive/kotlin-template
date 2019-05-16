package dk.nodes.template.domain.interactors

import dk.nodes.template.models.Post
import dk.nodes.template.repositories.PostRepository
import java.io.IOException
import javax.inject.Inject

class PostsInteractor @Inject constructor(
    private val postRepository: PostRepository
) : BaseAsyncInteractor<List<Post>> {

    override suspend fun invoke(): List<Post> {
        val posts = postRepository.getPosts(true)
        if (posts.isEmpty()) {
            throw IOException("Empty posts")
        }
        return posts
    }
}