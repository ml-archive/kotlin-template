package dk.eboks.app.initializers

import android.app.Application

interface AppInitializer {
    fun init(app: Application)
}

class AppInitializers(vararg params: AppInitializer) : AppInitializer {
    private val initializers = params.asList()
    override fun init(app: Application) {
        initializers.forEach {
            it.init(app)
        }
    }
}