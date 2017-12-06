package dk.nodes.template

import android.app.Application
import android.util.Log
import dk.nodes.nstack.kotlin.NStack
import dk.nodes.template.domain.models.Translation
import dk.nodes.template.injection.DaggerAppComponent
import dk.nodes.template.injection.components.AppComponent
import dk.nodes.template.injection.modules.AppModule
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

        NStack.setLogFunction { tag, msg -> Log.e(tag, msg) }
        NStack.setTranslationClass(Translation::class.java)

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