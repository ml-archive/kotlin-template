package dk.nodes.template.injection.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import dk.nodes.arch.domain.injection.scopes.AppScope
import dk.nodes.template.App

@Module
class AppModule(private val application: App) {
    @Provides
    @AppScope
    fun provideContext(): Context {
        return application.baseContext
    }

    @Provides
    @AppScope
    fun provideApp(): App {
        return application
    }
}