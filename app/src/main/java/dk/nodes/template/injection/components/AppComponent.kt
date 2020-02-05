package dk.nodes.template.injection.components

import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import dk.nodes.template.App
import dk.nodes.template.injection.modules.AppModule
import dk.nodes.template.injection.modules.InteractorModule
import dk.nodes.template.injection.modules.RestModule
import dk.nodes.template.injection.modules.RestRepositoryBinding
import dk.nodes.template.injection.modules.StorageBindingModule
import dk.nodes.template.presentation.injection.PresentationModule
import dk.nodes.template.presentation.injection.ViewModelBuilder
import javax.inject.Singleton

@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        ViewModelBuilder::class,
        PresentationModule::class,
        AppModule::class,
        InteractorModule::class,
        RestModule::class,
        RestRepositoryBinding::class,
        StorageBindingModule::class
    ]
)
@Singleton
interface AppComponent : AndroidInjector<App> {
    @Component.Factory
    abstract class Factory : AndroidInjector.Factory<App>
}