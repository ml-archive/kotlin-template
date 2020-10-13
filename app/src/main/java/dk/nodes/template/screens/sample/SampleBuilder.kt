package dk.nodes.template.screens.sample

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import dk.nodes.template.injection.modules.ViewModelKey

@Module
abstract class SampleBuilder {

    @ContributesAndroidInjector
    abstract fun sampleFragment(): SampleFragment

    @Binds
    @IntoMap
    @dk.nodes.template.injection.modules.ViewModelKey(SampleViewModel::class)
    internal abstract fun bindSampleViewModel(viewModel: SampleViewModel): ViewModel
}