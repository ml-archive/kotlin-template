package dk.nodes.template.injection.modules

import dagger.Binds
import dagger.Module
import dk.nodes.template.data.network.RestPostRepository
import dk.nodes.template.domain.repositories.PostRepository
import javax.inject.Singleton

@Module
abstract class RestRepositoryBinding {
    @Binds
    @Singleton
    abstract fun bindPostRepository(repository: RestPostRepository): PostRepository
}