package dk.eboks.app.initializers

import android.app.Application
import dk.eboks.app.domain.config.AppConfig
import dk.eboks.app.domain.models.Translation
import dk.nodes.nstack.kotlin.NStack
import dk.nodes.nstack.kotlin.util.NLog
import javax.inject.Inject

class NStackInitializer @Inject constructor(private val appConfig: AppConfig) : AppInitializer {
    override fun init(app: Application) {
        if (!appConfig.isDebug)
            NStack.customRequestUrl = appConfig.currentMode.customTranslationUrl

        NStack.translationClass = Translation::class.java
        NStack.debugMode = appConfig.isDebug
        NStack.debugLogLevel = NLog.Level.VERBOSE
        // Set custom translation url if not in debug
        // NStack.customRequestUrl = AppConfigImpl.currentMode.customTranslationUrl
        NStack.init(app)
    }
}