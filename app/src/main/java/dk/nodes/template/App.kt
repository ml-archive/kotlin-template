package dk.nodes.template

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

import dk.nodes.template.inititializers.AppInitializer
import javax.inject.Inject

@HiltAndroidApp
class App : Application() {

    @Inject
    lateinit var initializer: AppInitializer
    override fun onCreate() {
        super.onCreate()
        instance = this
        initializer.init(this)
    }

    companion object {
        lateinit var instance: App
        private set
    }
}