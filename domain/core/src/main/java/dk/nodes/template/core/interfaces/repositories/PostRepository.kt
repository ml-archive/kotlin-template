package dk.nodes.template.core.interfaces.repositories

import dk.nodes.template.core.entities.Post
import kotlinx.coroutines.flow.Flow

interface PostRepository {
    suspend fun getPosts(): List<Post>
    fun getPostsFlow(): Flow<List<Post>>
}