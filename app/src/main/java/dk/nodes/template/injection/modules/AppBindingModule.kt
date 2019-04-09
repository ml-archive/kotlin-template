package dk.nodes.template.injection.modules

import dagger.Binds
import dagger.Module
import dk.nodes.template.inititializers.AppInitializer
import dk.nodes.template.inititializers.AppInitializerImpl

@Module
abstract class AppBindingModule {
    @Binds
    abstract fun bindAppInitalizer(initializer: AppInitializerImpl): AppInitializer
}