package dk.nodes.template.injection.modules

import dagger.Binds
import dagger.Module
import dk.nodes.template.domain.interactors.GetPostsInteractor
import dk.nodes.template.domain.interactors.GetPostsInteractorImpl

@Module
abstract class InteractorModule {
    @Binds
    abstract fun bindGetPostsInteractor(interactor: GetPostsInteractorImpl): GetPostsInteractor
}