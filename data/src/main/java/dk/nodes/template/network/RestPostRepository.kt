package dk.nodes.template.network

import dk.nodes.template.models.Post
import dk.nodes.template.repositories.PostRepository
import dk.nodes.template.repositories.RepositoryException
import javax.inject.Inject

class RestPostRepository @Inject constructor(private val api: Api) : PostRepository {
    @Throws(RepositoryException::class)
    override suspend fun getPosts(cached: Boolean): List<Post> {
        val response = api.getPosts().execute()
        if (response.isSuccessful) {
            return response.body()
                ?: throw(RepositoryException(
                    response.code(),
                    response.message()
                ))
        }
        throw(RepositoryException(response.code(), response.message()))
    }
}