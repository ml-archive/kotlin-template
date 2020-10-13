package dk.nodes.data.network

import dk.nodes.data.dto.PostDto
import retrofit2.Response
import retrofit2.http.GET

interface Api {
    @GET("posts")
    suspend fun getPosts(): Response<List<PostDto>>
}
