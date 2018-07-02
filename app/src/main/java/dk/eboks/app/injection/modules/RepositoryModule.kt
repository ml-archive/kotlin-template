package dk.eboks.app.injection.modules

import android.content.Context
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dk.eboks.app.domain.managers.CacheManager
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
    fun provideSignupRestRepository(context: Context, api: Api) : SignupRestRepository
    {
        return SignupRestRepository(context, api)
    }

    @Provides
    @AppScope
    fun provideUserRestRepository(context: Context, api: Api, gson: Gson) : UserRestRepository
    {
        return UserRestRepository(context, api, gson)
    }

    @Provides
    @AppScope
    fun provideMessagesRepository(context: Context, api: Api, gson: Gson, cacheManager : CacheManager) : MessagesRepository
    {
        return MessagesRestRepository(context, api, gson, cacheManager)
    }

    @Provides
    @AppScope
    fun provideSendersRepository(context: Context, api: Api, gson: Gson, cacheManager : CacheManager) : SendersRepository {
        return SendersRestRepository(context, api, gson, cacheManager)
    }

    @Provides
    @AppScope
    fun provideSenderCategoriesRepository(context: Context, api: Api, gson: Gson, cacheManager : CacheManager) : SenderCategoriesRepository {
        return SenderCategoriesRestRepository(context, api, gson, cacheManager)
    }

    @Provides
    @AppScope
    fun provideMailCategoriesRepository(context: Context, api: Api, gson: Gson, cacheManager : CacheManager) : MailCategoriesRepository
    {
        return MailCategoriesRestRepository(context, api, gson, cacheManager)
    }

    @Provides
    @AppScope
    fun provideSettingsRepository(prefManager: PrefManager) : SettingsRepository
    {
        return SharedPrefsSettingsRepository(prefManager)
    }

    @Provides
    @AppScope
    fun provideFoldersRepository(context: Context, api: Api, gson: Gson, cacheManager : CacheManager) : FoldersRepository
    {
        return FoldersRestRepository(context, api, gson, cacheManager)
    }

    @Provides
    @AppScope
    fun provideChannelsRepository(context: Context, api: Api, gson: Gson, cacheManager : CacheManager) : ChannelsRepository
    {
        return ChannelsRestRepository(context, api, gson, cacheManager)
    }

    @Provides
    @AppScope
    fun provideCollectionsRepository(context: Context, api: Api, gson: Gson, cacheManager : CacheManager) : CollectionsRepository {
        return CollectionsRestRepository(context, api, gson, cacheManager)
    }
}