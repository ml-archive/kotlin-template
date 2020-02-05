package dk.nodes.template.presentation.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.android.support.DaggerFragment
import dk.nodes.template.presentation.extensions.getSharedViewModel
import dk.nodes.template.presentation.extensions.getViewModel
import dk.nodes.template.presentation.extensions.lifecycleAwareLazy
import dk.nodes.template.presentation.util.ViewErrorController
import javax.inject.Inject

abstract class BaseFragment : DaggerFragment() {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject lateinit var defaultErrorController: dagger.Lazy<ViewErrorController>

    protected inline fun <reified VM : ViewModel> getViewModel(): VM =
        getViewModel(viewModelFactory)

    protected inline fun <reified VM : ViewModel> getSharedViewModel(): VM =
        getSharedViewModel(viewModelFactory)

    protected inline fun <reified VM : ViewModel> viewModel(): Lazy<VM> = lifecycleAwareLazy(this) {
        getViewModel<VM>()
    }

    protected inline fun <reified VM : ViewModel> sharedViewModel(): Lazy<VM> =
        lifecycleAwareLazy(this) {
            getSharedViewModel<VM>()
        }
}
