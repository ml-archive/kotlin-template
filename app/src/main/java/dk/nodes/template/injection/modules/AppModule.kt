package dk.nodes.template.injection.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import dk.nodes.arch.domain.injection.scopes.AppScope
import dk.nodes.arch.util.AppCoroutineDispatchers
import dk.nodes.arch.util.AppRxSchedulers
import dk.nodes.template.App
import dk.nodes.template.inititializers.AppInitializers
import dk.nodes.template.inititializers.NStackInitializer
import dk.nodes.template.inititializers.TimberInitializer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.rx2.asCoroutineDispatcher

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

    @AppScope
    @Provides
    fun provideRxSchedulers(): AppRxSchedulers = AppRxSchedulers(
        io = Schedulers.io(),
        computation = Schedulers.computation(),
        main = AndroidSchedulers.mainThread()
    )

    @AppScope
    @Provides
    fun provideCoroutineDispatchers(schedulers: AppRxSchedulers) = AppCoroutineDispatchers(
        io = Dispatchers.IO,
        computation = schedulers.computation.asCoroutineDispatcher(),
        main = Dispatchers.Main
    )
}