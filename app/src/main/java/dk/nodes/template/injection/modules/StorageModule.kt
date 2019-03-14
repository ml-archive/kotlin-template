package dk.nodes.template.injection.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import dk.nodes.arch.domain.injection.scopes.AppScope
import dk.nodes.template.domain.managers.PrefManager
import dk.nodes.template.storage.PrefManagerImpl

@Module
class StorageModule {
    @Provides
    @AppScope
    fun providePrefManager(context: Context): PrefManager {
        return PrefManagerImpl(context)
    }
}