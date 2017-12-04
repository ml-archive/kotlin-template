package dk.eboks.app.network

/**
 * Created by bison on 24-06-2017.
 */
class RestPostRepository(val api: dk.eboks.app.network.Api) : dk.eboks.app.domain.repositories.PostRepository {
    override fun getPosts(): List<dk.eboks.app.domain.models.Post>
    {
        val response = api.getPosts().execute()
        if(response.isSuccessful)
        {
            return response.body()!!
        }
        throw(dk.eboks.app.domain.repositories.RepositoryException(response.code(), response.message()))
    }


}