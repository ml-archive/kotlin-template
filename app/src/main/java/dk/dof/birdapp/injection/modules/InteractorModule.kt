package dk.dof.birdapp.injection.modules

import dagger.Module
import dagger.Provides
import dk.dof.birdapp.domain.interactors.GetPostsInteractor
import dk.dof.birdapp.domain.interactors.GetPostsInteractorImpl
import dk.dof.birdapp.domain.repositories.PostRepository
import dk.nodes.arch.domain.executor.Executor


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