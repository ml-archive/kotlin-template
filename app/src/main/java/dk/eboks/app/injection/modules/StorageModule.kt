package dk.eboks.app.injection.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.managers.PrefManager
import dk.eboks.app.domain.managers.ResourceManager
import dk.eboks.app.domain.repositories.AppStateRepository
import dk.eboks.app.storage.AppStateManagerImpl
import dk.eboks.app.storage.PrefManagerImpl
import dk.eboks.app.storage.ResourceManagerImpl
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
}