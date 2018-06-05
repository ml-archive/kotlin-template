package dk.dof.birdapp.presentation.base

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import dk.dof.birdapp.App
import dk.dof.birdapp.injection.modules.PresentationModule
import dk.nodes.arch.presentation.base.BaseView

abstract class BaseFragment : Fragment(), BaseView {
    open val component by lazy {
        App.instance().appComponent.plus(PresentationModule())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    fun getBaseActivity() : BaseActivity?
    {
        if(activity is BaseActivity)
            return activity as BaseActivity
        return null
    }



}