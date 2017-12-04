package dk.nodes.arch.presentation.base

import android.os.Bundle
import android.support.v4.app.Fragment

abstract class BaseFragment<V> : Fragment(), BaseView {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        retainInstance = true

        //Make sure all of our dependencies get injected
        injectDependencies()
    }

    override fun onStart() {
        super.onStart()

        setupTranslations()
    }

    protected abstract fun injectDependencies()
}