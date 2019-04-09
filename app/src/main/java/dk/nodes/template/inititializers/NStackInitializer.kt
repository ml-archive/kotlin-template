package dk.nodes.template.inititializers

import android.app.Application
import dk.nodes.nstack.kotlin.NStack
import dk.nodes.template.nstack.Translation
import javax.inject.Inject

class NStackInitializer @Inject constructor() : AppInitializer {
    override fun init(app: Application) {
        NStack.translationClass = Translation::class.java
        NStack.init(app)
    }
}