package dk.nodes.data.network.repositories

import dk.nodes.data.dto.mapPosts
import dk.nodes.data.extensions.mapException
import dk.nodes.data.network.Api
import dk.nodes.template.core.entities.Post
import dk.nodes.template.core.interfaces.repositories.PostRepository
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import javax.inject.Inject
import kotlin.jvm.Throws

class RestPostRepository @Inject constructor(private val api: Api) : PostRepository {

    private val postsChannel = ConflatedBroadcastChannel<List<Post>>(emptyList())

    override fun getPostsFlow(): Flow<List<Post>> {
        return postsChannel.asFlow()
    }

    @Throws
    override suspend fun getPosts(): List<Post> {
        val response = api.getPosts()
        if (response.isSuccessful) {
            return response.body()
                ?.mapPosts()
                ?.also {
                    postsChannel.send(it)
                }
                ?: throw(response.mapException())
        } else {
            throw(response.mapException())
        }
    }
}