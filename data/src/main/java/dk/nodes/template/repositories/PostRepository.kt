package dk.nodes.template.repositories

import dk.nodes.template.models.Post

interface PostRepository {
    suspend fun getPosts(cached: Boolean = false): List<Post>
    suspend fun savePost(post: Post)
}