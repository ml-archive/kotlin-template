package dk.nodes.template.injection.modules

import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module

@Module
abstract class ViewModelBuilder {

    @Binds
    internal abstract fun bindViewModelFactory(factory: dk.nodes.template.injection.modules.DaggerViewModelFactory): ViewModelProvider.Factory
}