package dk.nodes.template

import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import dk.nodes.template.inititializers.AppInitializers
import dk.nodes.template.injection.components.DaggerAppComponent
import javax.inject.Inject

class App : DaggerApplication() {

    @Inject lateinit var initializers: AppInitializers
    override fun onCreate() {
        super.onCreate()
        initializers.init(this)
    }

    // uncomment me if multidex

    /*
    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
    */
    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder().create(this)
    }
}