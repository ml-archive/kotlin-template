package dk.nodes.template.injection.modules

import dagger.Binds
import dagger.Module
import dk.nodes.data.storage.PrefManagerImpl
import dk.nodes.template.core.interfaces.managers.PrefManager
import javax.inject.Singleton

@Module
abstract class StorageBindingModule {

    @Binds
    @Singleton
    abstract fun bindPrefManager(manager: PrefManagerImpl): PrefManager
}