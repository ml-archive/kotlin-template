package dk.nodes.template.network

import dk.nodes.template.domain.models.Photo
import dk.nodes.template.domain.models.Post
import retrofit2.Call
import retrofit2.http.GET

/**
 * Created by bison on 20-05-2017.
 */

interface Api {
    @GET("posts") fun getPosts() : Call<List<Post>>
    @GET("photos") fun getPhotos() : Call<List<Photo>>
}
