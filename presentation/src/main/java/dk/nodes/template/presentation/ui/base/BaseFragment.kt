package dk.nodes.template.presentation.ui.base

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import dagger.android.support.DaggerFragment
import dk.nodes.template.presentation.extensions.getSharedViewModel
import dk.nodes.template.presentation.extensions.getViewModel
import dk.nodes.template.presentation.extensions.lifecycleAwareLazy
import dk.nodes.template.presentation.util.ViewErrorController
import javax.inject.Inject

abstract class BaseFragment : DaggerFragment() {


    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

    val defaultErrorHandler: ViewErrorController by lazy {
        ViewErrorController(context = requireContext())
    }

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
