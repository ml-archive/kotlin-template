package dk.nodes.template.inititializers

import android.app.Application
import dk.nodes.nstack.kotlin.NStack
import dk.nodes.template.BuildConfig
import dk.nodes.template.domain.managers.ThemeManager
import dk.nodes.template.presentation.nstack.Translation
import dk.nodes.template.presentation.util.ThemeHelper
import timber.log.Timber
import javax.inject.Inject

interface AppInitializer {
    fun init(app: Application)
}

class AppInitializerImpl @Inject constructor(private val themeManager: ThemeManager) : AppInitializer {
    override fun init(app: Application) {
        NStack.translationClass = Translation::class.java
        NStack.init(app, BuildConfig.DEBUG)
        if (BuildConfig.DEBUG) {
            NStack.enableLiveEdit(app)
            Timber.plant(Timber.DebugTree())
        }
        ThemeHelper.applyTheme(themeManager.theme)
    }
}