package dk.dof.birdapp

import android.app.Application
import dk.dof.birdapp.domain.models.Translation
import dk.dof.birdapp.injection.components.AppComponent
import dk.dof.birdapp.injection.components.DaggerAppComponent
import dk.dof.birdapp.injection.modules.AppModule
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
        _instance = this

        if(BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        appComponent.inject(this)

        setupNstack()
    }

    private fun setupNstack() {
        NStack.translationClass = Translation::class.java
        NStack.init(this)
        NStack.debugMode = true
    }

    // uncomment me if multidex
    /*
    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
    */

    companion object {
        private lateinit var _instance : App
        fun instance() : App
        {
            return _instance
        }
    }

}