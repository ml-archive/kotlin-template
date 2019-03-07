package dk.eboks.app.injection.components

import dagger.Component
import dk.eboks.app.App
import dk.eboks.app.injection.modules.AppModule
import dk.eboks.app.injection.modules.ExecutorModule
import dk.eboks.app.injection.modules.InteractorModule
import dk.eboks.app.injection.modules.RepositoryModule
import dk.eboks.app.injection.modules.StorageModule
import dk.eboks.app.injection.modules.UtilModule
import dk.eboks.app.network.RestModule
import dk.nodes.arch.domain.injection.scopes.AppScope

@Component(
    modules = [
        AppModule::class,
        ExecutorModule::class,
        InteractorModule::class,
        RestModule::class,
        RepositoryModule::class,
        StorageModule::class,
        UtilModule::class
    ]
)

@AppScope
interface AppComponent {
    fun inject(app: App)

    // For Interactor Injection
    fun plus(): PresentationComponent
}