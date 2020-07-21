package dk.nodes.template.injection.modules

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dk.nodes.template.inititializers.AppInitializer
import dk.nodes.template.inititializers.AppInitializerImpl

@Module
@InstallIn(ApplicationComponent::class)
interface AppModule {

    @Binds
    fun bindAppInitalizer(initializer: AppInitializerImpl): AppInitializer
}
