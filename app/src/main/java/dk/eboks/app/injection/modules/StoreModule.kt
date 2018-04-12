package dk.eboks.app.injection.modules

import android.content.Context
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dk.eboks.app.domain.models.SenderCategory
import dk.eboks.app.domain.models.channel.Channel
import dk.eboks.app.domain.models.folder.Folder
import dk.eboks.app.domain.models.message.Message
import dk.eboks.app.domain.models.sender.CollectionContainer
import dk.eboks.app.domain.models.sender.Sender
import dk.eboks.app.network.Api
import dk.eboks.app.storage.base.CacheStore
import dk.nodes.arch.domain.injection.scopes.AppScope

/**
 * Created by bison on 01/02/18.
 */

// new ones
typealias FolderIdMessageStore = CacheStore<Long, List<Message>>
typealias FolderTypeMessageStore = CacheStore<String, List<Message>>
typealias ChannelListStore = CacheStore<String, MutableList<Channel>>
typealias FolderListStore = CacheStore<Int, List<Folder>>
typealias MailCategoryStore = CacheStore<Long, List<Folder>>
typealias CollectionsStore = CacheStore<Int, List<CollectionContainer>>
typealias SenderStore = CacheStore<Int, List<Sender>>
typealias SenderCategoryStore = CacheStore<String, List<SenderCategory>>


@Module
class StoreModule {

    @Provides
    @AppScope
    fun provideFolderIdMessageStore(api: Api, gson : Gson, context : Context) : FolderIdMessageStore
    {
        return FolderIdMessageStore(context, gson, "folder_id_message_store.json", { key ->
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
        return FolderTypeMessageStore(context, gson, "folder_type_message_store.json", { key ->
            val response = api.getMessagesByType(key).execute()
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
    fun provideChannelListStore(api: Api, gson : Gson, context : Context) : ChannelListStore
    {
        return ChannelListStore(context, gson, "channel_list_store.json", { key ->
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
    fun provideFolderListStore(api: Api, gson : Gson, context : Context) : FolderListStore
    {
        return FolderListStore(context, gson, "folder_list_store.json", { key ->
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
        return MailCategoryStore(context, gson, "mail_category_store.json", { key ->
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
        return CollectionsStore(context, gson, "collectons_store.json", { key ->
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
        return SenderStore(context, gson, "sender_store.json", { key ->
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
        return SenderCategoryStore(context, gson, "sender_category_store.json", { key ->
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