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
import javax.inject.Named

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
    fun provideMessagesRepository(context: Context, api: Api, gson: Gson) : MessagesRepository
    {
        return MessagesRestRepository(context, api, gson)
    }

    @Provides
    @AppScope
    fun provideSendersRepository(context: Context, api: Api, gson: Gson) : SendersRepository {
        return SendersRestRepository(context, api, gson)
    }

    @Provides
    @AppScope
    fun provideSenderCategoriesRepository(context: Context, api: Api, gson: Gson) : SenderCategoriesRepository {
        return SenderCategoriesRestRepository(context, api, gson)
    }

    @Provides
    @AppScope
    fun provideMailCategoriesRepository(context: Context, api: Api, gson: Gson) : MailCategoriesRepository
    {
        return MailCategoriesRestRepository(context, api, gson)
    }

    @Provides
    @AppScope
    fun provideSettingsRepository(prefManager: PrefManager) : SettingsRepository
    {
        return SharedPrefsSettingsRepository(prefManager)
    }

    @Provides
    @AppScope
    fun provideFoldersRepository(context: Context, api: Api, gson: Gson) : FoldersRepository
    {
        return FoldersRestRepository(context, api, gson)
    }

    @Provides
    @AppScope
    fun provideChannelsRepository(context: Context, api: Api, gson: Gson) : ChannelsRepository
    {
        return ChannelsRestRepository(context, api, gson)
    }

    @Provides
    @AppScope
    fun provideCollectionsRepository(context: Context, api: Api, gson: Gson) : CollectionsRepository {
        return CollectionsRestRepository(context, api, gson)
    }
}