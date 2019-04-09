package dk.nodes.template.inititializers

import android.app.Application
import dk.nodes.nstack.kotlin.NStack
import dk.nodes.template.BuildConfig
import dk.nodes.template.presentation.nstack.Translation
import timber.log.Timber
import javax.inject.Inject

interface AppInitializer {
    fun init(app: Application)
}

class AppInitializerImpl @Inject constructor() : AppInitializer {
    override fun init(app: Application) {
        NStack.translationClass = Translation::class.java
        NStack.init(app)
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}