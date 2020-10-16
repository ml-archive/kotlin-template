package dk.nodes.template.injection.modules

import dagger.Binds
import dagger.Module
import dk.nodes.data.network.repositories.RestPostRepository
import dk.nodes.template.core.interfaces.repositories.PostRepository
import javax.inject.Singleton

@Module
abstract class RestRepositoryBinding {
    @Binds
    @Singleton
    abstract fun bindPostRepository(repository: RestPostRepository): PostRepository
}