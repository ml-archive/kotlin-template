package dk.nodes.template.injection.modules

import dk.nodes.template.inititializers.AppInitializer
import dk.nodes.template.inititializers.AppInitializerImpl
import org.koin.dsl.module

val appModule = module {
    single<AppInitializer> { AppInitializerImpl() }
}