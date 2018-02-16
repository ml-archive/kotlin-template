package dk.eboks.app.injection.modules

import dagger.Module
import dagger.Provides
import dk.eboks.app.domain.managers.GuidManager
import dk.eboks.app.system.managers.GuidManagerImpl
import dk.nodes.arch.domain.injection.scopes.AppScope

/**
 * Created by bison on 15/12/17.
 */
@Module
class UtilModule {
    @Provides
    @AppScope
    fun provideGuidManager() : GuidManager
    {
        return GuidManagerImpl()
    }
}