package dk.eboks.app.injection.components

import dagger.Component
import dk.eboks.app.App
import dk.eboks.app.domain.interactors.*
import dk.eboks.app.domain.managers.AppStateManager
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
    fun appStateManager() : AppStateManager
    fun loginInteractor() : LoginInteractor
    fun boostrapInteractor() : BootstrapInteractor
    fun getSendersInteractor() : GetSendersInteractor
    fun getCategoriesInteractor() : GetCategoriesInteractor
    fun getMessagesInteractor() : GetMessagesInteractor
    fun getFoldersInteractor() : GetFoldersInteractor
}