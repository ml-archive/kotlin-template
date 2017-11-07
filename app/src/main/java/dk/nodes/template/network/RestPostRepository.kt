package dk.nodes.template.network

import dk.nodes.template.domain.models.Post
import dk.nodes.template.domain.repositories.PostRepository
import dk.nodes.template.domain.repositories.RepositoryException

/**
 * Created by bison on 24-06-2017.
 */
class RestPostRepository(val api: dk.nodes.template.network.Api) : PostRepository {
    override fun getPosts(): List<Post>
    {
        val response = api.getPosts().execute()
        if(response.isSuccessful)
        {
            return response.body()!!
        }
        throw(RepositoryException(response.code(), response.message()))
    }


}