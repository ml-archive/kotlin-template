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
        initializer.init(this)
    }
}