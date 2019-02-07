package dk.eboks.app.injection.modules

import android.content.Context
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.managers.CacheManager
import dk.eboks.app.domain.managers.PrefManager
import dk.eboks.app.domain.repositories.AppStateRepository
import dk.eboks.app.domain.repositories.ChannelsRepository
import dk.eboks.app.domain.repositories.CollectionsRepository
import dk.eboks.app.domain.repositories.FoldersRepository
import dk.eboks.app.domain.repositories.MailCategoriesRepository
import dk.eboks.app.domain.repositories.MessagesRepository
import dk.eboks.app.domain.repositories.SenderCategoriesRepository
import dk.eboks.app.domain.repositories.SendersRepository
import dk.eboks.app.domain.repositories.SettingsRepository
import dk.eboks.app.domain.repositories.UserRepository
import dk.eboks.app.network.Api
import dk.eboks.app.network.repositories.ChannelsRestRepository
import dk.eboks.app.network.repositories.CollectionsRestRepository
import dk.eboks.app.network.repositories.FoldersRestRepository
import dk.eboks.app.network.repositories.MailCategoriesRestRepository
import dk.eboks.app.network.repositories.MessagesRestRepository
import dk.eboks.app.network.repositories.SenderCategoriesRestRepository
import dk.eboks.app.network.repositories.SendersRestRepository
import dk.eboks.app.network.repositories.SignupRestRepository
import dk.eboks.app.network.repositories.UserRestRepository
import dk.eboks.app.storage.repositories.AppStateRepositoryImpl
import dk.eboks.app.storage.repositories.SharedPrefsSettingsRepository
import dk.nodes.arch.domain.injection.scopes.AppScope
import okhttp3.OkHttpClient

/**
 * Created by bison on 25-07-2017.
 */
@Module
class RepositoryModule {
    @Provides
    @AppScope
    fun provideAppStateRepository(context: Context, gson: Gson): AppStateRepository {
        return AppStateRepositoryImpl(context, gson)
    }

    @Provides
    @AppScope
    fun provideSignupRestRepository(context: Context, api: Api): SignupRestRepository {
        return SignupRestRepository(api)
    }

    @Provides
    @AppScope
    fun provideUserRestRepository(context: Context, api: Api, gson: Gson): UserRepository {
        return UserRestRepository(context, api, gson)
    }

    @Provides
    @AppScope
    fun provideMessagesRepository(
        context: Context,
        api: Api,
        gson: Gson,
        cacheManager: CacheManager,
        httpClient: OkHttpClient,
        appState: AppStateManager
    ): MessagesRepository {
        return MessagesRestRepository(context, api, gson, cacheManager, httpClient, appState)
    }

    @Provides
    @AppScope
    fun provideSendersRepository(
        context: Context,
        api: Api,
        gson: Gson,
        cacheManager: CacheManager
    ): SendersRepository {
        return SendersRestRepository(context, api, gson, cacheManager)
    }

    @Provides
    @AppScope
    fun provideSenderCategoriesRepository(
        context: Context,
        api: Api,
        gson: Gson,
        cacheManager: CacheManager
    ): SenderCategoriesRepository {
        return SenderCategoriesRestRepository(context, api, gson, cacheManager)
    }

    @Provides
    @AppScope
    fun provideMailCategoriesRepository(
        context: Context,
        api: Api,
        gson: Gson,
        cacheManager: CacheManager
    ): MailCategoriesRepository {
        return MailCategoriesRestRepository(context, api, gson, cacheManager)
    }

    @Provides
    @AppScope
    fun provideSettingsRepository(prefManager: PrefManager): SettingsRepository {
        return SharedPrefsSettingsRepository(prefManager)
    }

    @Provides
    @AppScope
    fun provideFoldersRepository(
        context: Context,
        api: Api,
        gson: Gson,
        cacheManager: CacheManager
    ): FoldersRepository {
        return FoldersRestRepository(context, api, gson, cacheManager)
    }

    @Provides
    @AppScope
    fun provideChannelsRepository(
        context: Context,
        api: Api,
        gson: Gson,
        cacheManager: CacheManager
    ): ChannelsRepository {
        return ChannelsRestRepository(context, api, gson, cacheManager)
    }

    @Provides
    @AppScope
    fun provideCollectionsRepository(
        context: Context,
        api: Api,
        gson: Gson,
        cacheManager: CacheManager
    ): CollectionsRepository {
        return CollectionsRestRepository(context, api, gson, cacheManager)
    }
}