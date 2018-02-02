package dk.eboks.app.injection.components

import dagger.Component
import dk.eboks.app.App
import dk.eboks.app.domain.interactors.BootstrapInteractor
import dk.eboks.app.domain.interactors.GetFoldersInteractor
import dk.eboks.app.domain.interactors.GetSendersInteractor
import dk.eboks.app.domain.interactors.LoginInteractor
import dk.eboks.app.injection.modules.*
import dk.eboks.app.network.Api
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.injection.scopes.AppScope

@Component(modules = [
    AppModule::class,
    ExecutorModule::class,
    InteractorModule::class,
    RestModule::class,
    StoreModule::class,
    RepositoryModule::class,
    PresentationModule::class,
    StorageModule::class,
    UtilModule::class])

@AppScope
interface AppComponent
{
    fun inject(app: App)

    // expose functions to components dependent on this component
    fun executor() : Executor
    fun api() : Api
    fun loginInteractor() : LoginInteractor
    fun boostrapInteractor() : BootstrapInteractor
    fun getSendersInteractor() : GetSendersInteractor
    fun getFoldersInteractor() : GetFoldersInteractor
}