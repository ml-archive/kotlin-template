package dk.eboks.app.presentation.base

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import dk.nodes.arch.presentation.base.BaseView

abstract class BaseActivity : AppCompatActivity(), BaseView {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Make sure all of our dependencies get injected
        injectDependencies()
    }

    override fun onStart() {
        super.onStart()

        setupTranslations()
    }

    protected abstract fun injectDependencies()
}