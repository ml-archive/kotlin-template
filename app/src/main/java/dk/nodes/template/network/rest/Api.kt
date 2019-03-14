package dk.nodes.template.network.rest

import dk.nodes.template.domain.Photo
import dk.nodes.template.domain.Post
import io.reactivex.Single
import okio.BufferedSource
import retrofit2.Call
import retrofit2.http.GET

interface Api {
    @GET("posts")
    fun getPosts(): Call<List<dk.nodes.template.domain.Post>>

    @GET("photos")
    fun getPhotos(): Call<List<dk.nodes.template.domain.Photo>>

    @GET("posts")
    fun getPostsBuffered(): Single<BufferedSource>
}
