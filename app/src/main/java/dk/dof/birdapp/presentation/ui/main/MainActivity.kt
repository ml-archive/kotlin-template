package dk.dof.birdapp.presentation.ui.main

import android.os.Bundle
import android.util.Log
import dk.dof.birdapp.R
import dk.dof.birdapp.presentation.base.BaseActivity
import dk.dof.birdapp.presentation.ui.map.MapComponentFragment
import dk.nodes.nstack.kotlin.NStack
import dk.nodes.nstack.kotlin.models.AppUpdateState
import javax.inject.Inject

class MainActivity : BaseActivity(), MainContract.View {


    @Inject lateinit var presenter: MainContract.Presenter

    override fun injectDependencies() {
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        addFragmentOnTop(R.id.contentFl,MapComponentFragment(),false)

        setupNstack()
    }


    private fun setupNstack() {
        NStack.onAppUpdateListener = { appUpdate ->
            when (appUpdate.state) {
                AppUpdateState.NONE      -> {
                    // Do nothing because there is no update
                }
                AppUpdateState.UPDATE    -> {
                    // Show a user a dialog that is dismissible
                }
                AppUpdateState.FORCE     -> {
                    // Show the user an undismissable dialog
                }
                AppUpdateState.CHANGELOG -> {
                    // Show change log (Not yet implemented because its never used)
                }
            }
        }
        NStack.appOpen({ success -> Log.e("debug", "appopen success = $success") })
    }


}
