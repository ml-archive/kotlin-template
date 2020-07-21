package dk.nodes.template.injection.modules

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dk.nodes.template.domain.managers.PrefManager
import dk.nodes.template.domain.managers.ThemeManager
import dk.nodes.template.domain.managers.ThemeManagerImpl
import dk.nodes.template.data.storage.PrefManagerImpl
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
interface StorageBindingModule {

    @Binds
    @Singleton
    fun bindPrefManager(manager: PrefManagerImpl): PrefManager

    @Binds
    @Singleton
    fun bindThemeManager(manager: ThemeManagerImpl): ThemeManager
}