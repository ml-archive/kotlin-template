package dk.nodes.template.injection.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import dk.nodes.template.App
import dk.nodes.template.inititializers.AppInitializers
import dk.nodes.template.inititializers.NStackInitializer
import dk.nodes.template.inititializers.TimberInitializer

@Module
class AppModule {
    @Provides
    fun provideContext(application: App): Context = application.applicationContext

    @Provides
    fun provideInitializers(
        nStackInitializer: NStackInitializer,
        timberInitializer: TimberInitializer
    ): AppInitializers {
        return AppInitializers(nStackInitializer, timberInitializer)
    }
}