package dk.nodes.template

import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import dk.nodes.template.inititializers.AppInitializer
import dk.nodes.template.injection.components.DaggerAppComponent
import javax.inject.Inject

class App : DaggerApplication() {

    @Inject lateinit var initializer: AppInitializer
    override fun onCreate() {
        super.onCreate()
        initializer.init(this)
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.factory().create(this)
    }
}