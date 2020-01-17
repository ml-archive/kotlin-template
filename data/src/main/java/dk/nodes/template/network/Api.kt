package dk.nodes.template.network

import dk.nodes.template.domain.models.Photo
import dk.nodes.template.domain.models.Post
import retrofit2.Response
import retrofit2.http.GET

interface Api {
    @GET("posts")
    suspend fun getPosts(): Response<List<Post>>

    @GET("photos")
    suspend fun getPhotos(): Response<List<Photo>>
}
