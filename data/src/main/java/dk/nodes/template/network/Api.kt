package dk.nodes.template.network

import dk.nodes.template.models.Photo
import dk.nodes.template.models.Post
import retrofit2.Response
import retrofit2.http.GET

interface Api {
    @GET("posts")
    suspend fun getPosts(): Response<List<Post>>

    @GET("photos")
    suspend fun getPhotos(): Response<List<Photo>>
}
