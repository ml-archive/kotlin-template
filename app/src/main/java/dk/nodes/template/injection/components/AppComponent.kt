package dk.nodes.template.injection.components

import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import dk.nodes.arch.domain.injection.scopes.AppScope
import dk.nodes.template.App
import dk.nodes.template.injection.modules.AppModule
import dk.nodes.template.injection.modules.ExecutorModule
import dk.nodes.template.injection.modules.InteractorModule
import dk.nodes.template.injection.modules.RestModule
import dk.nodes.template.injection.modules.RestRepositoryModule
import dk.nodes.template.injection.modules.StorageModule
import dk.nodes.template.injection.modules.ViewModelBuilder

@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        ViewModelBuilder::class,
        AppModule::class,
        ExecutorModule::class,
        InteractorModule::class,
        RestModule::class,
        RestRepositoryModule::class,
        StorageModule::class,
        TasksModule::class
    ]
)
@AppScope
interface AppComponent : AndroidInjector<App> {
    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<App>()
}