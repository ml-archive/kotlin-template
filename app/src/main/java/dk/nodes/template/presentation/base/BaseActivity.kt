package dk.nodes.template.presentation.base

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dk.nodes.arch.presentation.base.BaseView
import dk.nodes.nstack.kotlin.inflater.NStackBaseContext
import dk.nodes.template.App
import dk.nodes.template.injection.modules.PresentationModule

abstract class BaseActivity : AppCompatActivity(), BaseView {
    open val component by lazy {
        // Todo investigate a better way to do this
        App.instance().appComponent.plus(PresentationModule())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Make sure all of our dependencies get injected
        injectDependencies()
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(NStackBaseContext(newBase))
    }

    protected abstract fun injectDependencies()
}