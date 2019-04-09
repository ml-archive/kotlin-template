package dk.nodes.template.injection.modules

import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dk.nodes.template.presentation.injection.DaggerViewModelFactory
import dk.nodes.template.main.MainActivityBuilder

@Module(
    includes = [
        MainActivityBuilder::class
    ]
)
internal abstract class ViewModelBuilder {

    @Binds
    internal abstract fun bindViewModelFactory(factory: DaggerViewModelFactory): ViewModelProvider.Factory
}