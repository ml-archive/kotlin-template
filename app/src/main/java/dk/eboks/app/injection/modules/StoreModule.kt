package dk.eboks.app.injection.modules

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nytimes.android.external.fs3.FileSystemPersister
import com.nytimes.android.external.fs3.filesystem.FileSystemFactory
import com.nytimes.android.external.store3.base.impl.Store
import com.nytimes.android.external.store3.base.impl.StoreBuilder
import com.nytimes.android.external.store3.middleware.GsonParserFactory
import dagger.Module
import dagger.Provides
import dk.eboks.app.domain.models.SenderCategory
import dk.eboks.app.domain.models.sender.Sender
import dk.eboks.app.domain.models.sender.CollectionContainer
import dk.eboks.app.domain.models.folder.Folder
import dk.eboks.app.domain.models.folder.FolderType
import dk.eboks.app.domain.models.message.Message
import dk.eboks.app.domain.models.channel.Channel
import dk.eboks.app.network.Api
import dk.nodes.arch.domain.injection.scopes.AppScope
import okio.BufferedSource

/**
 * Created by bison on 01/02/18.
 */

typealias SenderStore = Store<List<Sender>, Int>
typealias SenderCategoryStore = Store<List<SenderCategory>, Long>
typealias CollectionsStore = Store<List<CollectionContainer>, Int>

typealias MailCategoryStore = Store<List<Folder>, Long>
typealias FolderStore = Store<List<Folder>, Int>

data class ListMessageStoreKey(var folderId : Long)
typealias ListMessageStore = Store<List<Message>, ListMessageStoreKey>
typealias FolderTypeMessageStore = Store<List<Message>, FolderType>

data class MessageStoreKey(var folderId : Long, var id : String)
typealias MessageStore = Store<Message, MessageStoreKey>

typealias ListChannelStore = Store<List<Channel>, Long>

@Module
class StoreModule {

    @Provides
    @AppScope
    fun provideCollectionsStore(api: Api, gson : Gson, context : Context) : CollectionsStore {
        return StoreBuilder.parsedWithKey<Int, BufferedSource, List<CollectionContainer>>()
                .fetcher { key -> api.getCollections() }
                .persister(FileSystemPersister.create(FileSystemFactory.create(context.cacheDir), { key -> "Collections$key"}))
                .parser(GsonParserFactory.createSourceParser<List<CollectionContainer>>(gson, object : TypeToken<List<CollectionContainer>>() {}.type))
                .open()
    }

    @Provides
    @AppScope
    fun provideSenderStore(api: Api, gson : Gson, context : Context) : SenderStore
    {
        return StoreBuilder.parsedWithKey<Int, BufferedSource, List<Sender>>()
                .fetcher { key -> api.getSenders() }
                .persister(FileSystemPersister.create(FileSystemFactory.create(context.cacheDir), { key -> "Sender$key"}))
                .parser(GsonParserFactory.createSourceParser<List<Sender>>(gson, object : TypeToken<List<Sender>>() {}.type))
                .open()
    }

    @Provides
    @AppScope
    fun provideSenderCategoryStore(api: Api, gson : Gson, context : Context) : SenderCategoryStore {
        return StoreBuilder.parsedWithKey<Long, BufferedSource, List<SenderCategory>>()
                .fetcher { key -> api.getSenderCategories("private") }
                .persister(FileSystemPersister.create(FileSystemFactory.create(context.cacheDir), { key -> "SenderCategory$key"}))
                .parser(GsonParserFactory.createSourceParser<List<SenderCategory>>(gson, object : TypeToken<List<SenderCategory>>() {}.type))
                .open()
    }

    @Provides
    @AppScope
    fun provideMailCategoryStore(api: Api, gson : Gson, context : Context) : MailCategoryStore
    {
        return StoreBuilder.parsedWithKey<Long, BufferedSource, List<Folder>>()
                .fetcher { key -> api.getMailCategories() }
                .persister(FileSystemPersister.create(FileSystemFactory.create(context.cacheDir), { key -> "MailCategory$key"}))
                .parser(GsonParserFactory.createSourceParser<List<Folder>>(gson, object : TypeToken<List<Folder>>() {}.type))
                .open()
    }

    @Provides
    @AppScope
    fun provideListMessageStore(api: Api, gson : Gson, context : Context) : ListMessageStore
    {
        return StoreBuilder.parsedWithKey<ListMessageStoreKey, BufferedSource, List<Message>>()
                .fetcher { key -> api.getMessages(key.folderId) }
                .persister(FileSystemPersister.create(FileSystemFactory.create(context.cacheDir), { key -> "MessageList$key"}))
                .parser(GsonParserFactory.createSourceParser<List<Message>>(gson, object : TypeToken<List<Message>>() {}.type))
                .open()
    }


    @Provides
    @AppScope
    fun provideFolderTypeMessageStore(api: Api, gson : Gson, context : Context) : FolderTypeMessageStore
    {
        return StoreBuilder.parsedWithKey<FolderType, BufferedSource, List<Message>>()
                .fetcher { key -> api.getMessagesByType(key) }
                .persister(FileSystemPersister.create(FileSystemFactory.create(context.cacheDir), { key -> "FolderTypeMessage$key"}))
                .parser(GsonParserFactory.createSourceParser<List<Message>>(gson, object : TypeToken<List<Message>>() {}.type))
                .open()
    }

    @Provides
    @AppScope
    fun provideFolderStore(api: Api, gson : Gson, context : Context) : FolderStore
    {
        return StoreBuilder.parsedWithKey<Int, BufferedSource, List<Folder>>()
                .fetcher { key -> api.getFolders() }
                .persister(FileSystemPersister.create(FileSystemFactory.create(context.cacheDir), { key -> "Folder$key"}))
                .parser(GsonParserFactory.createSourceParser<List<Folder>>(gson, object : TypeToken<List<Folder>>() {}.type))
                .open()
    }

    @Provides
    @AppScope
    fun provideListChannelStore(api: Api, gson : Gson, context : Context) : ListChannelStore
    {
        return StoreBuilder.parsedWithKey<Long, BufferedSource, List<Channel>>()
                .fetcher { key -> api.getChannels() }
                .persister(FileSystemPersister.create(FileSystemFactory.create(context.cacheDir), { key -> "ChannelList$key"}))
                .parser(GsonParserFactory.createSourceParser<List<Channel>>(gson, object : TypeToken<List<Channel>>() {}.type))
                .open()
    }
}