package dk.nodes.template

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp
import dk.nodes.nstack.kotlin.inflater.NStackBaseContext

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

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(NStackBaseContext(base))
    }
}