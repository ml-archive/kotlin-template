package dk.eboks.app.injection.modules

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.Module
import dagger.Provides
import dk.eboks.app.domain.models.SenderCategory
import dk.eboks.app.domain.models.channel.Channel
import dk.eboks.app.domain.models.folder.Folder
import dk.eboks.app.domain.models.home.HomeContent
import dk.eboks.app.domain.models.message.Message
import dk.eboks.app.domain.models.sender.CollectionContainer
import dk.eboks.app.domain.models.sender.Sender
import dk.eboks.app.network.Api
import dk.eboks.app.storage.base.CacheStore
import dk.nodes.arch.domain.injection.scopes.AppScope
import javax.inject.Named

/**
 * Created by bison on 01/02/18.
 */

// new ones
typealias ChannelListStore = CacheStore<String, MutableList<Channel>>
typealias ChannelControlStore = CacheStore<Long, HomeContent>
typealias FolderListStore = CacheStore<Int, List<Folder>>
typealias MailCategoryStore = CacheStore<Long, List<Folder>>
typealias CollectionsStore = CacheStore<Int, List<CollectionContainer>>
typealias SenderStore = CacheStore<Int, List<Sender>>
typealias SenderCategoryStore = CacheStore<String, List<SenderCategory>>


typealias SenderIdMessageStore = CacheStore<Long, List<Message>>
typealias FolderIdMessageStore = CacheStore<Long, List<Message>>
typealias FolderTypeMessageStore = CacheStore<String, List<Message>>

@Module
class StoreModule {

    /*
    @Provides
    @AppScope
    fun provideFolderIdMessageStore(api: Api, gson : Gson, context : Context) : FolderIdMessageStore
    {
        return FolderIdMessageStore(context, gson, "folder_id_message_store.json", object : TypeToken<MutableMap<Long, List<Message>>>() {}.type, { key ->
            val response = api.getMessages(key).execute()
            var result : List<Message>? = null
            response?.let {
                if(it.isSuccessful)
                    result = it.body()
            }
            result
        })
    }

    @Provides
    @AppScope
    fun provideSenderIdMessageStore(api: Api, gson : Gson, context : Context) : SenderIdMessageStore
    {
        return SenderIdMessageStore(context, gson, "folder_id_message_store.json", object : TypeToken<MutableMap<Long, List<Message>>>() {}.type, { key ->
            val response = api.getMessages(key).execute()
            var result : List<Message>? = null
            response?.let {
                if(it.isSuccessful)
                    result = it.body()
            }
            result
        })
    }

    @Provides
    @AppScope
    fun provideFolderTypeMessageStore(api: Api, gson : Gson, context : Context) : FolderTypeMessageStore
    {
        return FolderTypeMessageStore(context, gson, "folder_type_message_store.json", object : TypeToken<MutableMap<String, List<Message>>>() {}.type, { key ->
            val response = api.getMessagesByType(key).execute()
            var result : List<Message>? = null
            response?.let {
                if(it.isSuccessful)
                    result = it.body()
            }
            result
        })
    }
    */


    @Provides
    @AppScope
    fun provideChannelListStore(api: Api, gson : Gson, context : Context) : ChannelListStore
    {
        return ChannelListStore(context, gson, "channel_list_store.json", object : TypeToken<MutableMap<String, MutableList<Channel>>>() {}.type, { key ->
            val response = if(key == "pinned") api.getChannelsPinned().execute() else api.getChannels().execute()
            var result : MutableList<Channel>? = null
            response?.let {
                if(it.isSuccessful)
                    result = it.body()
            }
            result
        })
    }

    @Provides
    @AppScope
    fun provideChannelControlStore(api: Api, gson : Gson, context : Context) : ChannelControlStore
    {
        return ChannelControlStore(context, gson, "channel_control_store.json", object : TypeToken<MutableMap<Long, HomeContent>>() {}.type, { key ->
            val response = api.getChannelHomeContent(key).execute()
            var result : HomeContent? = null
            response?.let {
                if(it.isSuccessful)
                    result = it.body()
            }
            result
        })
    }

    @Provides
    @AppScope
    fun provideFolderListStore(api: Api, gson : Gson, context : Context) : FolderListStore
    {
        return FolderListStore(context, gson, "folder_list_store.json", object : TypeToken<MutableMap<Int, List<Folder>>>() {}.type, { key ->
            val response = api.getFolders().execute()
            var result : List<Folder>? = null
            response?.let {
                if(it.isSuccessful)
                    result = it.body()
            }
            result
        })
    }

    @Provides
    @AppScope
    fun provideMailCategoryStore(api: Api, gson : Gson, context : Context) : MailCategoryStore
    {
        return MailCategoryStore(context, gson, "mail_category_store.json", object : TypeToken<MutableMap<Long, List<Folder>>>() {}.type, { key ->
            val response = api.getMailCategories().execute()
            var result : List<Folder>? = null
            response?.let {
                if(it.isSuccessful)
                    result = it.body()
            }
            result
        })
    }

    @Provides
    @AppScope
    fun provideCollectionsStore(api: Api, gson : Gson, context : Context) : CollectionsStore {
        return CollectionsStore(context, gson, "collectons_store.json", object : TypeToken<MutableMap<Int, List<CollectionContainer>>>() {}.type, { key ->
            val response = api.getCollections().execute()
            var result : List<CollectionContainer>? = null
            response?.let {
                if(it.isSuccessful)
                    result = it.body()
            }
            result
        })
    }

    @Provides
    @AppScope
    fun provideSenderStore(api: Api, gson : Gson, context : Context) : SenderStore
    {
        return SenderStore(context, gson, "sender_store.json", object : TypeToken<MutableMap<Int, List<Sender>>>() {}.type, { key ->
            val response = api.getSenders().execute()
            var result : List<Sender>? = null
            response?.let {
                if(it.isSuccessful)
                    result = it.body()
            }
            result
        })
    }

    @Provides
    @AppScope
    fun provideSenderCategoryStore(api: Api, gson : Gson, context : Context) : SenderCategoryStore {
        return SenderCategoryStore(context, gson, "sender_category_store.json", object : TypeToken<MutableMap<String, List<SenderCategory>>>() {}.type, { key ->
            val response = api.getSenderCategories(key).execute()
            var result : List<SenderCategory>? = null
            response?.let {
                if(it.isSuccessful)
                    result = it.body()
            }
            result
        })
    }

}