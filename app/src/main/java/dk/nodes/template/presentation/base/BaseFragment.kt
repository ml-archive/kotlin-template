package dk.nodes.template.presentation.base

import android.os.Bundle
import android.support.v4.app.Fragment
import dk.nodes.arch.presentation.base.BaseView
import dk.nodes.template.App
import dk.nodes.template.injection.modules.PresentationModule

abstract class BaseFragment : Fragment(), BaseView {
    open val component by lazy {
        // Todo investigate a better way to do this
        App.instance().appComponent.plus(PresentationModule())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        retainInstance = true

        //Make sure all of our dependencies get injected
        injectDependencies()
    }

    protected abstract fun injectDependencies()
}