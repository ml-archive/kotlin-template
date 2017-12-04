package dk.eboks.app.storage.injection

import android.content.Context
import dagger.Module
import dagger.Provides
import dk.nodes.arch.domain.injection.scopes.AppScope

/**
 * Created by bison on 25-07-2017.
 */
@Module
class StorageModule {
    @Provides
    @AppScope
    fun providePrefManager(context: Context) : dk.eboks.app.domain.managers.PrefManager
    {
        return dk.eboks.app.storage.PrefManagerImpl(context)
    }
}