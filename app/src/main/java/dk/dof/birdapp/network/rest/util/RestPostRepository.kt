package dk.dof.birdapp.network.rest

import dk.dof.birdapp.domain.models.Post
import dk.dof.birdapp.domain.repositories.PostRepository
import dk.dof.birdapp.domain.repositories.RepositoryException


/**
 * Created by bison on 24-06-2017.
 */
class RestPostRepository(val api: Api) : PostRepository {
    override fun getPosts(cached : Boolean): List<Post>
    {
        val response = api.getPosts().execute()
        if(response.isSuccessful)
        {
            return response.body()!!
        }
        throw(RepositoryException(response.code(), response.message()))
    }


}