package dk.eboks.app.injection.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import dk.eboks.app.domain.managers.*
import dk.eboks.app.system.managers.EboksFormatterImpl
import dk.eboks.app.system.managers.EncryptionPreferenceManagerImpl
import dk.eboks.app.system.managers.GuidManagerImpl
import dk.eboks.app.system.managers.permission.PermissionManagerImpl
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.injection.scopes.AppScope

/**
 * Created by bison on 15/12/17.
 */
@Module
class UtilModule {
    @Provides
    @AppScope
    fun provideEncryptionManager(context: Context): EncryptionPreferenceManager {
        return EncryptionPreferenceManagerImpl(context)
    }

    @Provides
    @AppScope
    fun provideGuidManager(): GuidManager {
        return GuidManagerImpl()
    }

    @Provides
    @AppScope
    fun provideEboksFormatter(context: Context): EboksFormatter {
        return EboksFormatterImpl(context)
    }

    @Provides
    @AppScope
    fun providePermissionManager(
            executor: Executor,
            context: Context,
            uiManager: UIManager
    ): PermissionManager {
        return PermissionManagerImpl(executor, context, uiManager)
    }
}