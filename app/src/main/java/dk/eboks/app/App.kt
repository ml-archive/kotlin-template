package dk.eboks.app

import android.app.Activity
import android.app.Application
import android.content.Context

import android.os.Bundle
import androidx.multidex.MultiDex
import dk.eboks.app.initializers.AppInitializers
import dk.eboks.app.injection.components.AppComponent
import dk.eboks.app.injection.components.DaggerAppComponent
import dk.eboks.app.injection.modules.AppModule

import java.lang.ref.WeakReference
import javax.inject.Inject

class App : Application(), Application.ActivityLifecycleCallbacks {
    val appComponent: AppComponent by lazy {
        DaggerAppComponent
            .builder()
            .appModule(AppModule(this))
            .build()
    }

    @Inject lateinit var appInitializers: AppInitializers

    override fun onCreate() {
        super.onCreate()
        appComponent.inject(this)
        appInitializers.init(this)
        registerActivityLifecycleCallbacks(this)
    }

    // uncomment me if multidex

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    private var activityRef: WeakReference<Activity?>? = null

    fun currentActivity(): Activity? {
        activityRef?.let {
            if (it.get() != null)
                return it.get()
        }
        return null
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