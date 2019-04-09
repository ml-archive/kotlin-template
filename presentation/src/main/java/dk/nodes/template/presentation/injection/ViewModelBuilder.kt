package dk.nodes.template.presentation.injection

import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dk.nodes.template.presentation.ui.main.MainActivityBuilder

@Module(
    includes = [
        MainActivityBuilder::class
    ]
)
abstract class ViewModelBuilder {

    @Binds
    internal abstract fun bindViewModelFactory(factory: DaggerViewModelFactory): ViewModelProvider.Factory
}