package dk.eboks.app.injection.modules

import android.content.Context
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dk.eboks.app.domain.managers.PrefManager
import dk.eboks.app.domain.repositories.*
import dk.eboks.app.network.Api
import dk.eboks.app.network.repositories.*
import dk.eboks.app.storage.repositories.AppStateRepositoryImpl
import dk.eboks.app.storage.repositories.SharedPrefsSettingsRepository
import dk.nodes.arch.domain.injection.scopes.AppScope

/**
 * Created by bison on 25-07-2017.
 */
@Module
class RepositoryModule {
    @Provides
    @AppScope
    fun provideAppStateRepository(context: Context, gson: Gson) : AppStateRepository
    {
        return AppStateRepositoryImpl(context, gson)
    }

    @Provides
    @AppScope
    fun provideMessagesRepository(api: Api, gson: Gson, folderIdMessageStore: FolderIdMessageStore, folderTypeMessageStore: FolderTypeMessageStore) : MessagesRepository
    {
        return MessagesRestRepository(api, gson, folderIdMessageStore, folderTypeMessageStore)
    }

    @Provides
    @AppScope
    fun provideSendersRepository(api: Api, gson: Gson, senderStore: SenderStore) : SendersRepository {
        return SendersRestRepository(api, gson, senderStore)
    }

    @Provides
    @AppScope
    fun provideSenderCategoriesRepository(api: Api, gson: Gson, senderCategoryStore: SenderCategoryStore) : SenderCategoriesRepository {
        return SenderCategoriesRestRepository(api, gson, senderCategoryStore)
    }

    @Provides
    @AppScope
    fun provideMailCategoriesRepository(mailCategoryStore: MailCategoryStore) : MailCategoriesRepository
    {
        return MailCategoriesRestRepository(mailCategoryStore)
    }

    @Provides
    @AppScope
    fun provideSettingsRepository(prefManager: PrefManager) : SettingsRepository
    {
        return SharedPrefsSettingsRepository(prefManager)
    }

    @Provides
    @AppScope
    fun provideFoldersRepository(folderStore: FolderListStore) : FoldersRepository
    {
        return FoldersRestRepository(folderStore)
    }

    @Provides
    @AppScope
    fun provideChannelsRepository(api: Api, gson: Gson, channelStore: ChannelListStore) : ChannelsRepository
    {
        return ChannelsRestRepository(api, gson, channelStore)
    }

    @Provides
    @AppScope
    fun provideCollectionsRepository(collectionStore: CollectionsStore) : CollectionsRepository {
        return CollectionsRestRepository(collectionStore)
    }
}