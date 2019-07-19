package dk.nodes.template.presentation.injection

import dagger.Module
import dk.nodes.template.presentation.ui.main.MainActivityBuilder
import dk.nodes.template.presentation.ui.sample.SampleBuilder
import dk.nodes.template.presentation.ui.splash.SplashBuilder

@Module(includes = [
    MainActivityBuilder::class,
    SplashBuilder::class,
    SampleBuilder::class
])
class PresentationModule