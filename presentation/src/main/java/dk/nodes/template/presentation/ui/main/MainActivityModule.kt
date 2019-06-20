package dk.nodes.template.presentation.ui.main

import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mainActivityModule = module {
    viewModel { MainActivityViewModel(get()) }
}