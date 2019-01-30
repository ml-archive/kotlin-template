package dk.nodes.template.network.rest

import dk.nodes.template.domain.models.Post
import dk.nodes.template.domain.repositories.PostRepository
import dk.nodes.template.domain.repositories.RepositoryException

class RestPostRepository(private val api: Api) : PostRepository {
    @Throws(RepositoryException::class)
    override suspend fun getPosts(cached: Boolean): List<Post> {
        val response = api.getPosts().execute()
        if (response.isSuccessful) {
            return response.body() ?: throw(RepositoryException(response.code(), response.message()))
        }
        throw(RepositoryException(response.code(), response.message()))
    }
}