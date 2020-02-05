package dk.nodes.template.presentation.ui.sample

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import dk.nodes.template.presentation.injection.ViewModelKey

@Module
abstract class SampleBuilder {

    @ContributesAndroidInjector
    abstract fun sampleFragment(): SampleFragment

    @Binds
    @IntoMap
    @ViewModelKey(SampleViewModel::class)
    internal abstract fun bindSampleViewModel(viewModel: SampleViewModel): ViewModel
}