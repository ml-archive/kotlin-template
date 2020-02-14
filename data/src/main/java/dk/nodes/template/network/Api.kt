package dk.nodes.template.network

import dk.nodes.template.domain.entities.Post
import retrofit2.Response
import retrofit2.http.GET

interface Api {
    @GET("posts")
    suspend fun getPosts(): Response<List<Post>>
}
