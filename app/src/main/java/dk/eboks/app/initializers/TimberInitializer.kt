package dk.eboks.app.initializers

import android.app.Application
import android.util.Log
import dk.eboks.app.domain.config.AppConfig
import timber.log.Timber
import javax.inject.Inject

class TimberInitializer @Inject constructor(private val appConfig: AppConfig) : AppInitializer {
    override fun init(app: Application) {
        if (appConfig.isDebug) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(HockeyTree)
        }
    }
}

object HockeyTree : Timber.Tree() {
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        if (priority == Log.ERROR) {
            try {
                val exceptionHandlerCls = Class.forName("net.hockeyapp.android.ExceptionHandler")
                try {
                    val defaultHandler =
                        exceptionHandlerCls.cast(Thread.getDefaultUncaughtExceptionHandler())
                    val method = exceptionHandlerCls.getMethod(
                        "uncaughtException",
                        Thread::class.java,
                        Throwable::class.java
                    )
                    method.invoke(defaultHandler, Thread.currentThread(), t)
                } catch (ex: ClassCastException) {
                    //e.printStackTrace()
                    Timber.d("Could not get HockeySDK uncaught exception handler")
                }
            } catch (e: ClassNotFoundException) {
                Timber.d("Could not load HockeyApp SDK Classes, cannot record crash")
            }
        }
    }
}