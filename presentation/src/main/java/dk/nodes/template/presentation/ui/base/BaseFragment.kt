package dk.nodes.template.presentation.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import dagger.android.support.DaggerFragment

import javax.inject.Inject

abstract class BaseFragment : DaggerFragment() {
    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

    protected inline fun <reified VM : ViewModel> viewModel(): Lazy<VM> = lazy {
        ViewModelProviders.of(this, viewModelFactory)
            .get(VM::class.java)
    }

    protected inline fun <reified VM : ViewModel> sharedViewModel(): Lazy<VM> = lazy {
        ViewModelProviders.of(requireActivity(), viewModelFactory)
            .get(VM::class.java)
    }
}