package dk.nodes.template.injection.modules

import dagger.Binds
import dagger.Module
import dk.nodes.template.domain.managers.PrefManager
import dk.nodes.template.domain.managers.ThemeManager
import dk.nodes.template.domain.managers.ThemeManagerImpl
import dk.nodes.template.data.storage.PrefManagerImpl
import javax.inject.Singleton

@Module
abstract class StorageBindingModule {

    @Binds
    @Singleton
    abstract fun bindPrefManager(manager: PrefManagerImpl): PrefManager

    @Binds
    @Singleton
    abstract fun bindThemeManager(manager: ThemeManagerImpl): ThemeManager
}