package dk.nodes.template.presentation.base

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import dagger.android.support.DaggerAppCompatActivity
import dk.nodes.nstack.kotlin.inflater.NStackBaseContext
import javax.inject.Inject

abstract class BaseActivity : DaggerAppCompatActivity() {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(NStackBaseContext(newBase))
    }

    protected inline fun <reified VM : ViewModel> bindViewModel(): VM {
        return ViewModelProviders.of(this, viewModelFactory)
            .get(VM::class.java)
    }
}