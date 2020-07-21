package dk.nodes.template.injection.modules

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dk.nodes.template.data.network.RestPostRepository
import dk.nodes.template.domain.repositories.PostRepository
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
interface RestRepositoryBinding {
    @Binds
    @Singleton
    fun bindPostRepository(repository: RestPostRepository): PostRepository
}