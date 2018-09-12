package dk.nodes.template.inititializers

import android.app.Application
import dk.nodes.template.BuildConfig
import timber.log.Timber
import javax.inject.Inject

class TimberInitializer @Inject constructor() : AppInitializer {
    override fun init(app: Application) {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}