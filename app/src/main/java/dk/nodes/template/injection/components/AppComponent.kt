package dk.nodes.template.injection.components

import dagger.Component
import dk.nodes.arch.domain.injection.scopes.AppScope
import dk.nodes.template.App
import dk.nodes.template.injection.modules.AppModule
import dk.nodes.template.injection.modules.ExecutorModule
import dk.nodes.template.injection.modules.InteractorModule
import dk.nodes.template.injection.modules.PresentationModule
import dk.nodes.template.injection.modules.RestModule
import dk.nodes.template.injection.modules.RestRepositoryModule
import dk.nodes.template.injection.modules.StorageModule

@Component(
    modules = [
        AppModule::class,
        ExecutorModule::class,
        InteractorModule::class,
        RestModule::class,
        RestRepositoryModule::class,
        StorageModule::class
    ]
)
@AppScope
interface AppComponent {
    fun inject(app: App)
    fun plus(presentationModule: PresentationModule): PresentationComponent
}