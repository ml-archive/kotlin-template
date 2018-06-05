package dk.dof.birdapp.injection.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import dk.dof.birdapp.domain.managers.PrefManager
import dk.dof.birdapp.storage.PrefManagerImpl
import dk.nodes.arch.domain.injection.scopes.AppScope


/**
 * Created by bison on 25-07-2017.
 */
@Module
class StorageModule {
    @Provides
    @AppScope
    fun providePrefManager(context: Context) : PrefManager
    {
        return PrefManagerImpl(context)
    }
}