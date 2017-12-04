package dk.eboks.app.injection

import dagger.Component
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.injection.scopes.AppScope

@Component(modules = arrayOf(
        dk.eboks.app.injection.AppModule::class,
        dk.eboks.app.domain.executor.injection.ExecutorModule::class,
        dk.eboks.app.domain.interactors.injection.InteractorModule::class,
        dk.eboks.app.network.rest.injection.RestModule::class,
        dk.eboks.app.network.rest.injection.RestRepositoryModule::class,
        dk.eboks.app.presentation.injection.PresentationModule::class,
        dk.eboks.app.storage.injection.StorageModule::class))

@AppScope
interface AppComponent
{
    fun inject(app: dk.eboks.app.App)

    // expose functions to components dependent on this component
    fun executor() : Executor
    fun api() : dk.eboks.app.network.Api
    fun getPostsInteractor() : dk.eboks.app.domain.interactors.GetPostsInteractor
}