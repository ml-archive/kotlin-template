package dk.nodes.template.data.network

import dk.nodes.template.domain.entities.Post
import dk.nodes.template.domain.repositories.PostRepository
import dk.nodes.template.domain.repositories.RepositoryException
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import javax.inject.Inject

class RestPostRepository @Inject constructor(private val api: Api) : PostRepository {

    private val postsChannel = BroadcastChannel<List<Post>>(Channel.CONFLATED)
        .also {
            it.offer(listOf())
        }

    override fun getPostsFlow(): Flow<List<Post>> {
        return postsChannel.asFlow()
    }

    override suspend fun getPosts(): List<Post> {
        val response = api.getPosts()
        if (response.isSuccessful) {

            return response.body()
                ?.also {
                    postsChannel.send(it)
                }
                ?: throw(RepositoryException(
                    response.code(),
                    response.errorBody()?.string(),
                    response.message()
                ))
        } else {
            throw(RepositoryException(
                response.code(),
                response.errorBody()?.string(),
                response.message()
            ))
        }
    }
}