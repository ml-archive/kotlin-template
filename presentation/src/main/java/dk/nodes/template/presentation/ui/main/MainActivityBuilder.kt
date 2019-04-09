package dk.nodes.template.presentation.ui.main

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import dk.nodes.template.presentation.injection.ViewModelKey

@Module
internal abstract class MainActivityBuilder {

    @Binds
    @IntoMap
    @ViewModelKey(MainActivityViewModel::class)
    abstract fun bindMainActivityViewMode(viewModel: MainActivityViewModel): ViewModel

    @ContributesAndroidInjector
    internal abstract fun mainActivity(): MainActivity
}