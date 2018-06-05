package dk.dof.birdapp.injection.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import dk.dof.birdapp.App
import dk.nodes.arch.domain.injection.scopes.AppScope



/**
 * Created by bison on 25/07/17.
 */
@Module
class AppModule(val application: App) {
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