package dk.eboks.app.network

import dk.eboks.app.domain.models.SenderCategory
import dk.eboks.app.domain.models.channel.Channel
import dk.eboks.app.domain.models.folder.FolderType
import dk.eboks.app.domain.models.message.Message
import dk.eboks.app.domain.models.protocol.AliasBody
import dk.eboks.app.domain.models.protocol.LoginRequest
import dk.eboks.app.domain.models.sender.Alias
import dk.eboks.app.domain.models.sender.Sender
import io.reactivex.Single
import okio.BufferedSource
import retrofit2.Call
import retrofit2.http.*

/**
 * Created by bison on 20-05-2017.
 */

interface Api {
    // @GET("regions") fun getRegions() : Call<List<Region>>
    @GET("api/mail/categories") fun getMailCategories() : Single<BufferedSource>
    @GET("api/mail/folders") fun getFolders() : Single<BufferedSource>
    @GET("api/folders/{id}/messages") fun getMessages(@Path("id") id : Long) : Single<BufferedSource>
    @GET("mail/folders/{folderId}/messages/{id}") fun getMessage(@Path("id") id : String, @Path("folderId") folderId : Long, @Query("receipt") receipt : Boolean? = null, @Query("terms") terms : Boolean? = null) : Call<Message>
    @GET("mail/{type}/messages") fun getMessagesByType(@Path("type") type : FolderType) : Single<BufferedSource>
    @GET("api/senders") fun getSenders() : Single<BufferedSource>
    @GET("api/channels") fun getChannels() : Single<BufferedSource>
    @GET("api/channels/{id}") fun getChannel(@Path("id") id : Long) : Call<Channel>
    @PUT("session") fun login(@Body body : LoginRequest) : Single<BufferedSource>

    @GET("api/groups/collections") fun getCollections() : Single<BufferedSource>
    @GET("api/groups/{segment}/categories") fun getSenderCategories(@Path("segment") segment: String ) : Single<BufferedSource> // private or public
    @GET("api/groups/categories/{id}/senders") fun getSenders(@Path("id") categoryId : Long) : Call<SenderCategory> // TODO: shouldn't this be called "/api/groups/private/categories/{id}" ??
    @GET("api/groups/senders") fun searchSenders(@Query("searchText") searchText : String) : Call<List<Sender>>
    @GET("api/groups/senders/{id}") fun getSenderDetail(@Path("id") senderId : Long) : Call<Sender>
    @PUT("api/groups/senders/{sId}/sendergroups/{gId}") fun registerSenderGroup(@Path("sId") senderId : Long, @Path("gId") groupId : Long, @Body aliasRegistrations : AliasBody) : Call<Any>
    fun registerSender(id : Long) : Call<Any> // TODO
    fun registerSegment(id : Long) : Call<Any> // TODO
}
