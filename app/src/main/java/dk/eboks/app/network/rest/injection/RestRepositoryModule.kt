package dk.eboks.app.network.rest.injection

import dagger.Module
import dagger.Provides
import dk.nodes.arch.domain.injection.scopes.AppScope

/**
 * Created by bison on 25-07-2017.
 */
@Module
class RestRepositoryModule {
    @Provides
    @AppScope
    fun providePostRepository(api: dk.eboks.app.network.Api) : dk.eboks.app.domain.repositories.PostRepository
    {
        return dk.eboks.app.network.RestPostRepository(api)
    }
}