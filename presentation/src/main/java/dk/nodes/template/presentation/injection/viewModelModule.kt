package dk.nodes.template.presentation.injection

import dk.nodes.template.presentation.ui.main.mainActivityModule
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

val viewModelModule = module {
    loadKoinModules(mainActivityModule)
}