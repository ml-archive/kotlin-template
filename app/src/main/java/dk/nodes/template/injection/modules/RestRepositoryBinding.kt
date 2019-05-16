package dk.nodes.template.injection.modules

import dagger.Binds
import dagger.Module
import dk.nodes.arch.domain.injection.scopes.AppScope
import dk.nodes.template.network.RestPostRepository
import dk.nodes.template.repositories.PostRepository

@Module
abstract class RestRepositoryBinding {
    @Binds
    @AppScope
    abstract fun bindPostRepository(repository: RestPostRepository): PostRepository
}