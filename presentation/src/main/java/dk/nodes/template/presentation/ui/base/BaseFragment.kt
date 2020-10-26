package dk.nodes.template.presentation.ui.base

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.findNavController
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import dagger.android.support.AndroidSupportInjection
import dk.nodes.template.presentation.extensions.getSharedViewModel
import dk.nodes.template.presentation.extensions.getViewModel
import dk.nodes.template.presentation.extensions.lifecycleAwareLazy
import dk.nodes.template.presentation.extensions.observeNonNull
import dk.nodes.template.presentation.navigation.FragmentNavigationHandler
import dk.nodes.template.presentation.navigation.Route
import dk.nodes.template.presentation.util.SingleEvent
import dk.nodes.template.presentation.util.ViewErrorController
import dk.nodes.template.presentation.util.consume
import javax.inject.Inject

abstract class BaseFragment : Fragment, HasAndroidInjector {

    constructor()
    constructor(@LayoutRes resId: Int) : super(resId)

    abstract val viewModel: BaseViewModel<*>

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var defaultErrorController: dagger.Lazy<ViewErrorController>

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

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


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.navigationLiveData.observeNonNull(viewLifecycleOwner) { navEvent ->
            handleNavigationEvent(navEvent)
        }
    }

    private fun handleNavigationEvent(navEvent: SingleEvent<Route>) = navEvent.consume { route ->
        val handler = FragmentNavigationHandler(this)
        handler.handle(route)
    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun androidInjector() = androidInjector
}