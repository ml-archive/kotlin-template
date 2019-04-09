package dk.nodes.template.network

import dk.nodes.template.models.Photo
import dk.nodes.template.models.Post
import retrofit2.Call
import retrofit2.http.GET

interface Api {
    @GET("posts")
    fun getPosts(): Call<List<Post>>

    @GET("photos")
    fun getPhotos(): Call<List<Photo>>
}
