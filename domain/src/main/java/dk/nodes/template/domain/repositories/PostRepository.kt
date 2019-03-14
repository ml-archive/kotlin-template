package dk.nodes.template.domain.repositories

import dk.nodes.template.domain.models.Post

interface PostRepository {
    suspend fun getPosts(cached: Boolean = false): List<Post>
}