package dk.nodes.template.injection.modules

import android.content.Context
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dk.nodes.template.data.storage.PrefManagerImpl
import dk.nodes.template.domain.managers.PrefManager
import dk.nodes.template.domain.managers.ThemeManager
import dk.nodes.template.domain.managers.ThemeManagerImpl
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
interface StorageBindingModule {

    @Binds
    @Singleton
    fun bindThemeManager(manager: ThemeManagerImpl): ThemeManager
}

@Module
@InstallIn(ApplicationComponent::class)
object StorageModule {
    @Provides
    @Singleton
    fun providePrefManager(@ApplicationContext context: Context): PrefManager {
        return PrefManagerImpl(context)
    }
}