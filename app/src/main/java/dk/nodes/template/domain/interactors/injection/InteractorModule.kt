package dk.nodes.template.domain.interactors.injection

import dagger.Module
import dagger.Provides
import dk.nodes.template.domain.executor.Executor
import dk.nodes.template.domain.interactors.GetPostsInteractor
import dk.nodes.template.domain.interactors.GetPostsInteractorImpl
import dk.nodes.template.domain.repositories.PostRepository

/**
 * Created by bison on 09/10/17.
 */
@Module
class InteractorModule {
    @Provides fun provideGetPostsInteractor(executor: Executor, postRepository: PostRepository) : GetPostsInteractor
    {
        return GetPostsInteractorImpl(executor, postRepository)
    }
}