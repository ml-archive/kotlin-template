package dk.nodes.template.presentation.injection

import dagger.Module
import dk.nodes.template.presentation.ui.main.MainActivityBuilder

@Module(includes = [
    MainActivityBuilder::class
])
class PresentationModule