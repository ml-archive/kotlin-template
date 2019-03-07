package dk.eboks.app.injection.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import dk.nodes.arch.domain.injection.scopes.AppScope
import dk.nodes.locksmith.core.preferences.EncryptedPreferences

/**
 * Created by bison on 25-07-2017.
 */
@Module(includes = [StorageBindingModule::class])
class StorageModule {

    @Provides
    @AppScope
    fun provideEncryptedPreferences(context: Context): EncryptedPreferences {
        return EncryptedPreferences(context, "EncryptedStorage", Context.MODE_PRIVATE)
    }
}