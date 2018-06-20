package dk.eboks.app.injection.modules

import android.content.Context
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dk.eboks.app.domain.managers.*
import dk.eboks.app.domain.repositories.AppStateRepository
import dk.eboks.app.storage.managers.*
import dk.eboks.app.system.managers.PrefManagerImpl
import dk.eboks.app.system.managers.ResourceManagerImpl
import dk.nodes.arch.domain.injection.scopes.AppScope

/**
 * Created by bison on 25-07-2017.
 */
@Module
class StorageModule {
    @Provides
    @AppScope
    fun providePrefManager(context: Context) : PrefManager
    {
        return PrefManagerImpl(context)
    }

    @Provides
    @AppScope
    fun provideResourceManager(context: Context) : ResourceManager
    {
        return ResourceManagerImpl(context)
    }

    @Provides
    @AppScope
    fun provideAppStateManager(appStateRepository: AppStateRepository) : AppStateManager
    {
        return AppStateManagerImpl(appStateRepository)
    }

    @Provides
    @AppScope
    fun provideFileCacheManager(context: Context, gson: Gson) : FileCacheManager
    {
        return FileCacheManagerImpl(context, gson)
    }

    @Provides
    @AppScope
    fun provideUserManager(context: Context, gson: Gson) : UserManager
    {
        return UserManagerImpl(context, gson)
    }

    @Provides
    @AppScope
    fun provideUserSettingsManager(context: Context, gson: Gson) : UserSettingsManager {
        return UserSettingsManagerImpl(context, gson)
    }

    @Provides
    @AppScope
    fun provideCacheManager(context: Context) : CacheManager
    {
        return CacheManagerImpl(context)
    }
}