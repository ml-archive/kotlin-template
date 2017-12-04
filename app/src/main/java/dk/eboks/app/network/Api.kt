package dk.eboks.app.network

import retrofit2.Call
import retrofit2.http.GET

/**
 * Created by bison on 20-05-2017.
 */

interface Api {
    @GET("posts") fun getPosts() : Call<List<dk.eboks.app.domain.models.Post>>
    @GET("photos") fun getPhotos() : Call<List<dk.eboks.app.domain.models.Photo>>
}
