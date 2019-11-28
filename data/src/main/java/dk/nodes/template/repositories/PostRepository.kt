package dk.nodes.template.repositories

import dk.nodes.template.domain.entities.Post
import kotlinx.coroutines.flow.Flow

interface PostRepository {
    suspend fun getPosts(): List<Post>
    fun getPostsFlow(): Flow<List<Post>>
}