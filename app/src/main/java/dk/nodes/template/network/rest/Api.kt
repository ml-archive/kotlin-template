package dk.nodes.template.network.rest

import dk.nodes.template.domain.models.Photo
import dk.nodes.template.domain.models.Post
import io.reactivex.Single
import okio.BufferedSource
import retrofit2.Call
import retrofit2.http.GET

interface Api {
    @GET("posts")
    fun getPosts(): Call<List<Post>>

    @GET("photos")
    fun getPhotos(): Call<List<Photo>>

    @GET("posts")
    fun getPostsBuffered(): Single<BufferedSource>
}
