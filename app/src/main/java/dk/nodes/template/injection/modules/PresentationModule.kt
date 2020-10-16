package dk.nodes.template.injection.modules

import dagger.Module
import dk.nodes.template.screens.main.MainActivityBuilder
import dk.nodes.template.screens.splash.SplashBuilder

@Module(includes = [
    MainActivityBuilder::class,
    SplashBuilder::class
])
class PresentationModule