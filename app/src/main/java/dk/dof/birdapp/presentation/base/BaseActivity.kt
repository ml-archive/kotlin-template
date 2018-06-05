package dk.dof.birdapp.presentation.base

import android.app.FragmentManager
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import dk.dof.birdapp.App
import dk.dof.birdapp.injection.modules.PresentationModule
import dk.nodes.arch.presentation.base.BaseView
import dk.nodes.nstack.kotlin.inflater.NStackBaseContext

abstract class BaseActivity : AppCompatActivity(), BaseView {
    open val component by lazy {
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

    /**
     * override this to indicate what primary navigation you belong to (only in screens with visible bottom navigation)
     */
    open fun getNavigationMenuAction(): Int {
        return -1
    }

    fun addFragmentOnTop(resId: Int, fragment: BaseFragment?, addToBack: Boolean = true) {
        fragment?.let {
            val trans = supportFragmentManager.beginTransaction().replace(resId, it)
            if (addToBack)
                trans.addToBackStack(null)
            trans.commit()
        }
    }
}