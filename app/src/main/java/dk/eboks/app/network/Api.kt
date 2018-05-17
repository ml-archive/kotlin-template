package dk.eboks.app.network

import dk.eboks.app.domain.models.SenderCategory
import dk.eboks.app.domain.models.channel.Channel
import dk.eboks.app.domain.models.channel.storebox.StoreboxReceipt
import dk.eboks.app.domain.models.channel.storebox.StoreboxReceiptItem
import dk.eboks.app.domain.models.folder.Folder
import dk.eboks.app.domain.models.formreply.ReplyForm
import dk.eboks.app.domain.models.home.HomeContent
import dk.eboks.app.domain.models.login.AccessToken
import dk.eboks.app.domain.models.message.Message
import dk.eboks.app.domain.models.protocol.AliasBody
import dk.eboks.app.domain.models.protocol.LoginRequest
import dk.eboks.app.domain.models.sender.CollectionContainer
import dk.eboks.app.domain.models.sender.Registrations
import dk.eboks.app.domain.models.sender.Segment
import dk.eboks.app.domain.models.sender.Sender
import dk.eboks.app.domain.models.shared.BooleanReply
import io.reactivex.Single
import okio.BufferedSource
import retrofit2.Call
import retrofit2.http.*

/**
 * Created by bison on 20-05-2017.
 */

interface Api {
    // login Mox
    @FormUrlEncoded
    @POST("http://authenticationservice20180404012549.azurewebsites.net/connect/token") fun getToken(@FieldMap bodyMap: Map<String, String>): Call<AccessToken>
    /*
        @FormUrlEncoded
    @POST("http://test401-oauth-api-dk.e-boks.com/connect/token") fun getToken(@FieldMap bodyMap: Map<String, String>): Call<AccessToken>
     */

    // user
    @GET("user/email/{email}") fun checkUserEmail(@Path("email") email : String) : Call<BooleanReply>
    @GET("user/identity/{identity}") fun checkUserIdentity(@Path("identity") cpr : String) : Call<BooleanReply>


    // @GET("regions") fun getRegions() : Call<List<Region>>
    @GET("mail/categories") fun getMailCategories() : Call<List<Folder>>
    @GET("mail/folders") fun getFolders() : Call<List<Folder>>
    @GET("mail/folders/{id}/messages") fun getMessages(@Path("id") id : Long) : Call<List<Message>>
    @GET("mail/messages/senders/{id}") fun getMessagesBySender(@Path("id") id : Long) : Call<List<Message>>
    @GET("mail/folders/{folderId}/messages/{id}") fun getMessage(@Path("id") id : String, @Path("folderId") folderId : Long, @Query("receipt") receipt : Boolean? = null, @Query("terms") terms : Boolean? = null) : Call<Message>
    @GET("mail/{type}/messages") fun getMessagesByType(@Path("type") type : String) : Call<List<Message>>
    @GET("mail/senders") fun getSenders() : Call<List<Sender>>
    @PUT("session") fun login(@Body body : LoginRequest) : Single<BufferedSource>

    // reply forms
    @GET("mail/folders/{folderId}/messages/{id}/reply") fun getMessageReplyForm(@Path("id") id : String, @Path("folderId") folderId : Long) : Call<ReplyForm>
    @PATCH("mail/folders/{folderId}/messages/{id}/reply") fun submitMessageReplyForm(@Path("id") id : String, @Path("folderId") folderId : Long, @Body body : ReplyForm) : Call<Any>

    // channels
    @GET("channels") fun getChannels() : Call<MutableList<Channel>>
    @GET("channels?pinned=true") fun getChannelsPinned() : Call<MutableList<Channel>>
    @GET("channels/{id}") fun getChannel(@Path("id") id : Long) : Call<Channel>
    @GET("channels/{id}/content/home") fun getChannelHomeContent(@Path("id") id : Long) : Call<HomeContent>
    @GET("channels/storebox/receipts") fun getStoreboxReceipts() : Call<List<StoreboxReceiptItem>>
    @GET("channels/storebox/receipts/{id}") fun getStoreboxReceipt(@Path("id") id : String) : Call<StoreboxReceipt>
    @POST("channels/storebox/user/signup/link") fun postLinkStorebox(@Body bodyMap: Map<String, String>) : Call<Any>
    @POST("channels/storebox/user/signup/link/activate") fun postActivateStorebox(@Body bodyMap: Map<String, String>) : Call<Any>

    // groups
    @GET("groups/registrations") fun getRegistrations() : Call<Registrations> // get all my registrations
    @GET("groups/registrations/pending/collections") fun getPendingRegistrations() : Call<List<CollectionContainer>>
    @GET("groups/collections") fun getCollections() : Call<List<CollectionContainer>> // for the Senders-landing page
    @GET("groups/{segment}/categories") fun getSenderCategories(@Path("segment") segment: String ) : Call<List<SenderCategory>> // private or public
    @GET("groups/categories/{id}/senders") fun getSenders(@Path("id") categoryId : Long) : Call<SenderCategory>   // TODO: shouldn't this be called "/api/groups/private/categories/{id}" ??
    @GET("groups/senders") fun searchSenders(@Query("searchText") searchText : String) : Call<List<Sender>>
    @GET("groups/segments/{id}") fun getSegmentDetail(@Path("id") segmentId : Long) : Call<Segment>               // segment detail
    @GET("groups/senders/{id}") fun getSenderDetail(@Path("id") senderId : Long) : Call<Sender>

    // register senders
    @PUT("groups/segments/{segId}") fun registerSegment(@Path("segId") segmentId : Long) : Call<Any>              // TODO should we have SegmentID or SegmentType?
    @PUT("groups/segments/{segType}") fun registerSegment(@Path("segType") segmentType : String) : Call<Any>      // TODO should we have SegmentType or SegmentID?
    @PUT("groups/senders/{id}") fun registerSender(@Path("id") senderId : Long) : Call<Any>
    @PUT("groups/senders/{sId}/sendergroups/{gId}") fun registerSenderGroup(@Path("sId") senderId : Long, @Path("gId") groupId : Long, @Body aliasRegistrations : AliasBody) : Call<Any>

    // un-register senders
    @DELETE("groups/segments/{segId}") fun unregisterSegment(@Path("segId") segmentId : Long) : Call<Any>         // TODO should we have SegmentID or SegmentType?
    @DELETE("groups/segments/{segType}") fun unregisterSegment(@Path("segType") segmentType : String) : Call<Any> // TODO should we have SegmentType or SegmentID?
    @DELETE("groups/senders/{id}") fun unregisterSender(@Path("id") senderId : Long) : Call<Any>
    @DELETE("groups/senders/{sId}/sendergroups/{gId}/alias/{aId}") fun unregisterSenderGroup(@Path("sId") senderId : Long, @Path("gId") groupId : Long) : Call<Any> // bodyless version // TODO check URL!!!
    //    @DELETE("groups/senders/{sId}/sendergroups/{gId}") fun unregisterSenderGroup(@Path("sId") senderId : Long, @Path("gId") groupId : Long, @Body aliasRegistrations : AliasBody) : Call<Any> // TODO: This needs to be a PATCH or POST or be bodyless as the one above
}
