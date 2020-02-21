package dk.nodes.template.injection.modules

import android.content.Context
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.coroutineScope
import dagger.Binds
import dagger.Module
import dagger.Provides
import dk.nodes.template.App
import dk.nodes.template.inititializers.AppInitializer
import dk.nodes.template.inititializers.AppInitializerImpl
import dk.nodes.template.injection.annotations.ProcessLifetime
import kotlinx.coroutines.CoroutineScope

@Module
abstract class AppModule {

    @Binds
    abstract fun bindAppInitalizer(initializer: AppInitializerImpl): AppInitializer

    companion object {
        @Provides
        fun provideContext(application: App): Context = application.applicationContext

        @Provides
        @ProcessLifetime
        fun provideLongLifetimeScope(): CoroutineScope {
            return ProcessLifecycleOwner.get().lifecycle.coroutineScope
        }
    }
}