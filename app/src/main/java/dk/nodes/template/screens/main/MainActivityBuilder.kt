package dk.nodes.template.screens.main

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import dk.nodes.template.injection.modules.ViewModelKey
import dk.nodes.template.screens.sample.SampleBuilder

@Module
internal abstract class MainActivityBuilder {

    @Binds
    @IntoMap
    @dk.nodes.template.injection.modules.ViewModelKey(MainActivityViewModel::class)
    abstract fun bindMainActivityViewMode(viewModel: MainActivityViewModel): ViewModel

    @ContributesAndroidInjector(
        modules = [
            SampleBuilder::class
        ]
    )
    internal abstract fun mainActivity(): MainActivity
}