package dk.nodes.template

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import dk.nodes.template.domain.modules.interactorModule
import dk.nodes.template.inititializers.AppInitializer
import dk.nodes.template.injection.modules.appModule
import dk.nodes.template.injection.modules.restModule
import dk.nodes.template.injection.modules.restRepositoryModule
import dk.nodes.template.injection.modules.storageModule
import dk.nodes.template.presentation.injection.viewModelModule
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    private val initializer by inject<AppInitializer>()
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(
                viewModelModule +
                appModule +
                restModule +
                restRepositoryModule +
                storageModule +
                interactorModule
            )
        }
        initializer.init(this)
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
}