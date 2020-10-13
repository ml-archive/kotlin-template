package dk.nodes.data.network.repositories

import dk.nodes.data.dto.mapPosts
import dk.nodes.data.extensions.mapException
import dk.nodes.data.network.Api
import dk.nodes.template.core.repositories.PostRepository
import dk.nodes.template.core.repositories.RepositoryException
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import javax.inject.Inject

class RestPostRepository @Inject constructor(private val api: Api) : dk.nodes.template.core.repositories.PostRepository {

    private val postsChannel = ConflatedBroadcastChannel<List<dk.nodes.template.core.entities.Post>>(emptyList())

    override fun getPostsFlow(): Flow<List<dk.nodes.template.core.entities.Post>> {
        return postsChannel.asFlow()
    }
    @Throws
    override suspend fun getPosts(): List<dk.nodes.template.core.entities.Post> {
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