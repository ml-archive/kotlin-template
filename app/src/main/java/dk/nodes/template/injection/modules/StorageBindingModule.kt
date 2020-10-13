package dk.nodes.template.injection.modules

import dagger.Binds
import dagger.Module
import dk.nodes.template.core.interfaces.managers.PrefManager
import dk.nodes.template.core.managers.ThemeManager
import dk.nodes.template.core.managers.ThemeManagerImpl
import dk.nodes.data.storage.PrefManagerImpl
import javax.inject.Singleton

@Module
abstract class StorageBindingModule {

    @Binds
    @Singleton
    abstract fun bindPrefManager(manager: PrefManagerImpl): PrefManager

    @Binds
    @Singleton
    abstract fun bindThemeManager(manager: dk.nodes.template.core.managers.ThemeManagerImpl): dk.nodes.template.core.managers.ThemeManager
}