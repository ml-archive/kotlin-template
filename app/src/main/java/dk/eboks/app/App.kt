package dk.eboks.app

import android.app.Application
import android.content.Context
import android.support.multidex.MultiDex
import android.util.Log
import dk.eboks.app.injection.components.AppComponent
import dk.eboks.app.injection.components.DaggerAppComponent
import dk.eboks.app.injection.modules.AppModule
import dk.nodes.nstack.kotlin.NStack
import timber.log.Timber

/**
 * Created by bison on 20-05-2017.
 */
class App : Application()
{
    val appComponent: AppComponent by lazy {
        DaggerAppComponent
                .builder()
                .appModule(AppModule(this))
                .build()
    }

    override fun onCreate() {
        super.onCreate()
        App.Companion._instance = this

        if(BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        appComponent.inject(this)
    }

    // uncomment me if multidex

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }


    companion object {
        private lateinit var _instance : App
        fun instance() : App
        {
            return App.Companion._instance
        }
    }

}