package dk.nodes.template.injection.modules

import dagger.Module
import dagger.Provides
import dk.nodes.arch.domain.injection.scopes.AppScope
import dk.nodes.template.domain.repositories.PostRepository
import dk.nodes.template.network.Api
import dk.nodes.template.network.RestPostRepository

/**
 * Created by bison on 25-07-2017.
 */
@Module
class RestRepositoryModule {
    @Provides
    @AppScope
    fun providePostRepository(api: Api) : PostRepository
    {
        return RestPostRepository(api)
    }
}