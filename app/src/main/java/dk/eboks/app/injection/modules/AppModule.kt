package dk.eboks.app.injection.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import dk.eboks.app.App
import dk.eboks.app.domain.managers.UIManager
import dk.eboks.app.presentation.managers.UIManagerImpl
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

    @AppScope
    @Provides
    fun provideUIManager(context: Context) : UIManager {
        return UIManagerImpl(context)
    }
}
