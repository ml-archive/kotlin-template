package dk.nodes.template.injection

import dagger.Component
import dk.nodes.template.App
import dk.nodes.template.domain.executor.Executor
import dk.nodes.template.domain.executor.injection.ExecutorModule
import dk.nodes.template.domain.interactors.GetPostsInteractor
import dk.nodes.template.domain.interactors.injection.InteractorModule
import dk.nodes.template.network.Api
import dk.nodes.template.network.rest.injection.RestModule
import dk.nodes.template.network.rest.injection.RestRepositoryModule
import dk.nodes.template.presentation.injection.PresentationModule
import dk.nodes.template.storage.injection.StorageModule

@Component(modules = arrayOf(
        AppModule::class,
        ExecutorModule::class,
        InteractorModule::class,
        RestModule::class,
        RestRepositoryModule::class,
        PresentationModule::class,
        StorageModule::class))

@ApplicationScope
interface AppComponent
{
    fun inject(app: App)

    // expose functions to components dependent on this component
    fun executor() : Executor
    fun api() : Api
    fun getPostsInteractor() : GetPostsInteractor
}