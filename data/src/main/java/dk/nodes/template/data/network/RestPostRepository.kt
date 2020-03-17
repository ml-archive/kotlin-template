package dk.nodes.template.data.network

import dk.nodes.template.domain.entities.Post
import dk.nodes.template.domain.repositories.PostRepository
import dk.nodes.template.domain.repositories.RepositoryException
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import retrofit2.Response
import javax.inject.Inject

class RestPostRepository @Inject constructor(private val api: Api) : PostRepository {

    private val postsChannel = ConflatedBroadcastChannel<List<Post>>(listOf())

    override fun getPostsFlow(): Flow<List<Post>> {
        return postsChannel.asFlow()
    }

    override suspend fun getPosts(): List<Post> = with(api.getPosts()) {
        return result.also {
            postsChannel.send(it)
        }
    }

    private val <T> Response<T>.result: T
        get() = body()?.let {
            return it
        } ?: throw(RepositoryException(
                code = code(),
                errorBody = errorBody()?.string(),
                msg = message()
        ))
}