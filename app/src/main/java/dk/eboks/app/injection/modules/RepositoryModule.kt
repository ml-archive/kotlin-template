package dk.eboks.app.injection.modules

import dagger.Module
import dagger.Provides
import dk.eboks.app.domain.managers.PrefManager
import dk.eboks.app.domain.repositories.PostRepository
import dk.eboks.app.domain.repositories.SettingsRepository
import dk.eboks.app.network.Api
import dk.eboks.app.network.RestPostRepository
import dk.eboks.app.storage.SharedPrefsSettingsRepository
import dk.nodes.arch.domain.injection.scopes.AppScope

/**
 * Created by bison on 25-07-2017.
 */
@Module
class RepositoryModule {
    @Provides
    @AppScope
    fun providePostRepository(api: Api) : PostRepository
    {
        return RestPostRepository(api)
    }

    @Provides
    @AppScope
    fun provideSettingsRepository(prefManager: PrefManager) : SettingsRepository
    {
        return SharedPrefsSettingsRepository(prefManager)
    }
}