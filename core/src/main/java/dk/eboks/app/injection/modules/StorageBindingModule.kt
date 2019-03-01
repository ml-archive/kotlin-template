package dk.eboks.app.injection.modules

import dagger.Binds
import dagger.Module
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.managers.CacheManager
import dk.eboks.app.domain.managers.FileCacheManager
import dk.eboks.app.domain.managers.PrefManager
import dk.eboks.app.domain.managers.ResourceManager
import dk.eboks.app.domain.managers.UserManager
import dk.eboks.app.domain.managers.UserSettingsManager
import dk.eboks.app.storage.managers.AppStateManagerImpl
import dk.eboks.app.storage.managers.CacheManagerImpl
import dk.eboks.app.storage.managers.FileCacheManagerImpl
import dk.eboks.app.storage.managers.UserManagerImpl
import dk.eboks.app.storage.managers.UserSettingsManagerImpl
import dk.eboks.app.system.managers.PrefManagerImpl
import dk.eboks.app.system.managers.ResourceManagerImpl
import dk.nodes.arch.domain.injection.scopes.AppScope

@Module
abstract class StorageBindingModule {
    @Binds
    @AppScope
    internal abstract fun bindPrefManager(manager: PrefManagerImpl): PrefManager

    @Binds
    @AppScope
    internal abstract fun bindResourceManager(manager: ResourceManagerImpl): ResourceManager

    @Binds
    @AppScope
    internal abstract fun bindAppStateManager(manager: AppStateManagerImpl): AppStateManager

    @Binds
    @AppScope
    internal abstract fun bindFileCacheManager(manager: FileCacheManagerImpl): FileCacheManager

    @Binds
    @AppScope
    internal abstract fun bindUserManager(manager: UserManagerImpl): UserManager

    @Binds
    @AppScope
    internal abstract fun bindUserSettingsManager(manager: UserSettingsManagerImpl): UserSettingsManager

    @Binds
    @AppScope
    internal abstract fun bindCacheManager(manager: CacheManagerImpl): CacheManager
}