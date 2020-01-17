package dk.nodes.template.network

import dk.nodes.template.network.dto.PostDto
import retrofit2.Response
import retrofit2.http.GET

interface Api {
    @GET("posts")
    suspend fun getPosts(): Response<List<PostDto>>
}
