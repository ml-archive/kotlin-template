package dk.nodes.template.injection.modules

import android.content.Context
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dk.nodes.template.inititializers.AppInitializer
import dk.nodes.template.inititializers.AppInitializerImpl

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Provides
    fun provideContext(@ApplicationContext context: Context) = context
}

@Module
@InstallIn(ApplicationComponent::class)
interface AppBindingModule {

    @Binds
    fun bindAppInitalizer(initializer: AppInitializerImpl): AppInitializer
}
