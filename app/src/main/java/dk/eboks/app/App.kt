package dk.eboks.app

import android.app.Activity
import android.app.Application
import android.os.Build
import android.os.Bundle
import dk.eboks.app.domain.config.Config
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.injection.components.AppComponent
import dk.eboks.app.injection.components.DaggerAppComponent
import dk.eboks.app.injection.modules.AppModule
import dk.nodes.locksmith.core.Locksmith
import dk.nodes.locksmith.core.models.LocksmithConfiguration
import dk.nodes.nstack.kotlin.NStack
import dk.nodes.nstack.kotlin.util.NLog
import timber.log.Timber
import java.lang.ref.WeakReference

class App : Application(), Application.ActivityLifecycleCallbacks {
    val appComponent: AppComponent by lazy {
        DaggerAppComponent
            .builder()
            .appModule(AppModule(this))
            .build()
    }

    override fun onCreate() {
        super.onCreate()

        App.Companion._instance = this
        // NStack.customRequestUrl = Config.currentMode.customTranslationUrl

        if (!BuildConfig.BUILD_TYPE.contains("debug", ignoreCase = true))
            NStack.customRequestUrl = Config.currentMode.customTranslationUrl

        NStack.translationClass = Translation::class.java
        NStack.debugMode = BuildConfig.BUILD_TYPE.contains("debug", ignoreCase = true)
        NStack.debugLogLevel = NLog.Level.VERBOSE
        // Set custom translation url if not in debug

        NStack.init(this)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Locksmith.init(this, LocksmithConfiguration(120))
//            Locksmith.instance.initFingerprint()
//            Locksmith.Builder(this)
//                    .setKeyValidityDuration(120)
//                    .setUseFingerprint(true)
//                    .build()
        } else {
            Locksmith.init(this, LocksmithConfiguration(120))
//            Locksmith.Builder(this)
//                    .build()
//                    .init()
        }

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        appComponent.inject(this)

        registerActivityLifecycleCallbacks(this)
    }

    // uncomment me if multidex

    /*
    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
    */

    companion object {
        private lateinit var _instance: App
        private var activityRef: WeakReference<Activity?>? = null

        fun instance(): App {
            return _instance
        }

        fun currentActivity(): Activity? {
            activityRef?.let {
                if (it.get() != null)
                    return it.get()
            }
            return null
        }
    }

    override fun onActivityPaused(activity: Activity?) {
        activityRef = null
    }

    override fun onActivityResumed(activity: Activity?) {
        activityRef = WeakReference(activity)
    }

    override fun onActivityStarted(p0: Activity?) {}
    override fun onActivityDestroyed(p0: Activity?) {}
    override fun onActivitySaveInstanceState(p0: Activity?, p1: Bundle?) {}
    override fun onActivityStopped(p0: Activity?) {}
    override fun onActivityCreated(p0: Activity?, p1: Bundle?) {}
}