package dk.nodes.template.injection.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import dk.nodes.template.App

@Module(
    includes = [
        AppBindingModule::class
    ]
)
class AppModule {
    @Provides
    fun provideContext(application: App): Context = application.applicationContext
}