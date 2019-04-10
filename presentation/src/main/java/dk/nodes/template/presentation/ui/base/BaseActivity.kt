package dk.nodes.template.presentation.ui.base

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
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