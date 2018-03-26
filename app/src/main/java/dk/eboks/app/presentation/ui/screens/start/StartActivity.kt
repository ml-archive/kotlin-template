package dk.eboks.app.presentation.ui.screens.start

import android.animation.LayoutTransition
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import dk.eboks.app.R
import dk.eboks.app.presentation.base.BaseActivity
import dk.eboks.app.presentation.ui.components.navigation.NavBarComponentFragment
import dk.eboks.app.presentation.ui.components.start.welcome.SplashComponentFragment
import dk.eboks.app.presentation.ui.screens.profile.ProfileActivity
import dk.nodes.nstack.kotlin.NStack
import dk.nodes.nstack.kotlin.models.AppUpdate
import dk.nodes.nstack.kotlin.models.AppUpdateState
import kotlinx.android.synthetic.main.activity_start.*
import timber.log.Timber
import javax.inject.Inject

class StartActivity : BaseActivity(), StartContract.View {
    @Inject
    lateinit var presenter: StartContract.Presenter

    var splashFragment: SplashComponentFragment? = SplashComponentFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(dk.eboks.app.R.layout.activity_start)

        component.inject(this)
        presenter.onViewCreated(this, lifecycle)

        addFragmentOnTop(R.id.containerFl, splashFragment, false)

        supportFragmentManager.addOnBackStackChangedListener {
            Timber.e("bs changed entryCount ${supportFragmentManager.backStackEntryCount}")
            if (supportFragmentManager.backStackEntryCount == 0) {
                if (!isDestroyed)
                    finish()
            }
        }
        presenter.startup()
    }

    override fun performVersionControl() {
        //NStack.appOpen({ success -> Timber.e("appopen success = $success") })
        NStack.appOpen()
        NStack.onAppUpdateListener = {
            Timber.e("ONAPPUPDATELISTENER")
            showUpdateDialog(it)
        }
    }

    private fun showUpdateDialog(appUpdate: AppUpdate) {
        if (appUpdate.state == AppUpdateState.NONE) {
            presenter.proceed()
            return
        }

        AlertDialog.Builder(this, R.style.AlertDialogNStackUpdate)
                .setTitle(appUpdate.title)
                .setMessage(appUpdate.message)
                .setPositiveButton(appUpdate.positiveBtn) { dialog, which ->
                    when (appUpdate.state) {
                        AppUpdateState.FORCE -> {
                            dialog.dismiss()
                            // TODO app should launch play intent taking you to the eboks package and do a check if the intent can be resolved and otherwise open in the external browser
                            // centralize this functionality in a helper class instead of dumping it all in here
                        }
                        else                 -> {
                            dialog.dismiss()
                            presenter.proceed()
                        }
                    }
                }
                .setNegativeButton(appUpdate.negativeBtn) { dialog, which ->
                }
                .show()
    }

    override fun showUserCarouselComponent() {
        splashFragment?.transitionToUserCarouselFragment()
        //setRootFragment(R.id.containerFl, UserCarouselComponentFragment())
    }

    override fun showWelcomeComponent() {
        splashFragment?.transitionToWelcomeFragment()
        //setRootFragment(R.id.containerFl, WelcomeComponentFragment())
    }

    override fun startMain() {
        //TODO change back to regular when done
        NavBarComponentFragment.currentMenuItem = R.id.actionMail
        startActivity(Intent(this, ProfileActivity::class.java))

        //overridePendingTransition(0, 0)
        finishAfterTransition()
    }

    fun removeSplashFragment() {
        Timber.e("Removing splash fragment")
        splashFragment?.let {
            supportFragmentManager.beginTransaction().remove(it).commit()
        }
        splashFragment = null
    }

    fun enableFragmentCheapFades() {
        containerFl.layoutTransition = LayoutTransition()
    }

    override fun onBackPressed() {
        Timber.e("on back bs count ${supportFragmentManager.backStackEntryCount}")
        if (supportFragmentManager.backStackEntryCount > 1) {
            supportFragmentManager.popBackStack()
        } else {
            finish()
        }
    }
}
