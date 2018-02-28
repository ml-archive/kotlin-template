package dk.eboks.app.network

import dk.eboks.app.domain.models.FolderType
import dk.eboks.app.domain.models.Message
import dk.eboks.app.domain.models.request.LoginRequest
import io.reactivex.Single
import okio.BufferedSource
import retrofit2.Call
import retrofit2.http.*

/**
 * Created by bison on 20-05-2017.
 */

interface Api {
    // @GET("regions") fun getRegions() : Call<List<Region>>
    @GET("api/mail/categories") fun getCategories() : Single<BufferedSource>
    @GET("api/mail/folders") fun getFolders() : Single<BufferedSource>
    @GET("api/folders/{id}/messages") fun getMessages(@Path("id") id : Long) : Single<BufferedSource>
    @GET("mail/folders/{folderId}/messages/{id}") fun getMessage(@Path("id") id : String, @Path("folderId") folderId : Long, @Query("receipt") receipt : Boolean? = null) : Call<Message>
    @GET("mail/{type}/messages") fun getMessagesByType(@Path("type") type : FolderType) : Single<BufferedSource>
    @GET("api/senders") fun getSenders() : Single<BufferedSource>
    @GET("api/channels") fun getChannels() : Single<BufferedSource>
    @PUT("session") fun login(@Body body : LoginRequest) : Single<BufferedSource>
}
