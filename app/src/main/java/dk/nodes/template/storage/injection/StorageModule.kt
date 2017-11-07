package dk.nodes.template.storage.injection

import android.content.Context
import dagger.Module
import dagger.Provides
import dk.nodes.template.domain.managers.PrefManager
import dk.nodes.template.injection.ApplicationScope
import dk.nodes.template.storage.PrefManagerImpl

/**
 * Created by bison on 25-07-2017.
 */
@Module
class StorageModule {
    @Provides
    @ApplicationScope
    fun providePrefManager(context: Context) : PrefManager
    {
        return PrefManagerImpl(context)
    }
}