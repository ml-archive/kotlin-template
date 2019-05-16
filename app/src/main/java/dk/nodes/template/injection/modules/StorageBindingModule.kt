package dk.nodes.template.injection.modules

import dagger.Binds
import dagger.Module
import dk.nodes.arch.domain.injection.scopes.AppScope
import dk.nodes.template.domain.managers.PrefManager
import dk.nodes.template.storage.PrefManagerImpl

@Module
abstract class StorageBindingModule {
    @Binds
    @AppScope
    abstract fun bindPrefManager(manager: PrefManagerImpl): PrefManager
}