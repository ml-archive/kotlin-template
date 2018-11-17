package dk.nodes.template.injection.modules

import dagger.Module
import dagger.Provides
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.template.domain.repositories.PostRepository

@Module
class InteractorModule {
    @Provides
    fun provideGetPostsInteractor(executor: Executor, postRepository: PostRepository): GetPostsInteractor {
        return GetPostsInteractorImpl(executor, postRepository)
    }
}