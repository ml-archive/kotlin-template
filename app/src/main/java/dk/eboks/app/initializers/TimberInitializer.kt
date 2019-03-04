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
        }
    }
}