package dk.nodes.template.presentation.ui.base

import android.content.Context
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.android.AndroidInjection
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import dk.nodes.nstack.kotlin.inflater.NStackBaseContext
import dk.nodes.template.presentation.extensions.getViewModel
import dk.nodes.template.presentation.extensions.lifecycleAwareLazy
import javax.inject.Inject

abstract class BaseActivity(@LayoutRes var resId: Int? = null) : AppCompatActivity(),
    HasAndroidInjector {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject lateinit var androidInjector: DispatchingAndroidInjector<Any>

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        resId?.let(this::setContentView)
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(NStackBaseContext(newBase))
    }

    protected inline fun <reified VM : ViewModel> getViewModel(): VM =
        getViewModel(viewModelFactory)

    protected inline fun <reified VM : ViewModel> viewModel(): Lazy<VM> {
        return lifecycleAwareLazy(this) { getViewModel<VM>() }
    }

    override fun androidInjector() = androidInjector
}
