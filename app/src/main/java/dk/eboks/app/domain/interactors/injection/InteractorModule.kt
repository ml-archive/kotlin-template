package dk.eboks.app.domain.interactors.injection

import dagger.Module
import dagger.Provides
import dk.nodes.arch.domain.executor.Executor

/**
 * Created by bison on 09/10/17.
 */
@Module
class InteractorModule {
    @Provides fun provideGetPostsInteractor(executor: Executor, postRepository: dk.eboks.app.domain.repositories.PostRepository) : dk.eboks.app.domain.interactors.GetPostsInteractor
    {
        return dk.eboks.app.domain.interactors.GetPostsInteractorImpl(executor, postRepository)
    }
}