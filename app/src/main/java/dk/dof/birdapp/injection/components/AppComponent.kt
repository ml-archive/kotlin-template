package dk.dof.birdapp.injection.components

import dagger.Component
import dk.dof.birdapp.App
import dk.dof.birdapp.domain.interactors.GetPostsInteractor
import dk.dof.birdapp.injection.modules.*
import dk.dof.birdapp.network.rest.Api
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.injection.scopes.AppScope


@Component(modules = arrayOf(
        AppModule::class,
        ExecutorModule::class,
        InteractorModule::class,
        RestModule::class,
        RestRepositoryModule::class,
        StorageModule::class))

@AppScope
interface AppComponent {
    fun inject(app: App)

    fun plus(presentationModule: PresentationModule): PresentationComponent

    // expose functions to components dependent on this component
    fun executor(): Executor

    fun api(): Api
    fun getPostsInteractor(): GetPostsInteractor
}