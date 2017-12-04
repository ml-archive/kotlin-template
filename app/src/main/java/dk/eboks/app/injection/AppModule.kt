package dk.eboks.app.injection

import android.content.Context
import dagger.Module
import dagger.Provides
import dk.nodes.arch.domain.injection.scopes.AppScope


/**
 * Created by bison on 25/07/17.
 */
@Module
class AppModule(val application: dk.eboks.app.App) {
    @Provides
    @AppScope
    fun provideContext(): Context {
        return application.baseContext
    }

    @Provides
    @AppScope
    fun provideApp(): dk.eboks.app.App {
        return application
    }
}