package dk.eboks.app.presentation.ui.start.screens

import android.animation.LayoutTransition
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import dk.eboks.app.BuildConfig
import dk.eboks.app.R
import dk.eboks.app.domain.config.Config
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.presentation.base.BaseActivity
import dk.eboks.app.presentation.ui.home.screens.HomeActivity
import dk.eboks.app.presentation.ui.login.components.LoginComponentFragment
import dk.eboks.app.presentation.ui.login.components.UserCarouselComponentFragment
import dk.eboks.app.presentation.ui.navigation.components.NavBarComponentFragment
import dk.eboks.app.presentation.ui.start.components.disclaimer.BetaDisclaimerComponentFragment
import dk.eboks.app.presentation.ui.start.components.signup.CompletedComponentFragment
import dk.eboks.app.presentation.ui.start.components.welcome.SplashComponentFragment
import dk.eboks.app.presentation.ui.start.components.welcome.WelcomeComponentFragment
import dk.eboks.app.util.BroadcastReceiver
import dk.eboks.app.util.setVisible
import dk.nodes.nstack.kotlin.NStack
import dk.nodes.nstack.kotlin.managers.ConnectionManager
import dk.nodes.nstack.kotlin.models.AppUpdate
import dk.nodes.nstack.kotlin.models.AppUpdateState
import kotlinx.android.synthetic.main.activity_start.*
import net.hockeyapp.android.CrashManager
import net.hockeyapp.android.CrashManagerListener
import net.hockeyapp.android.UpdateManager
import timber.log.Timber
import javax.inject.Inject

class StartActivity : BaseActivity(), StartContract.View {
    @Inject
    lateinit var presenter: StartContract.Presenter

    var splashFragment: SplashComponentFragment? = SplashComponentFragment()
    var disclaimerFragment: BetaDisclaimerComponentFragment? = BetaDisclaimerComponentFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(dk.eboks.app.R.layout.activity_start)

        component.inject(this)
        presenter.onViewCreated(this, lifecycle)

        if(intent.getBooleanExtra("sessionExpired", false)) {
            Timber.e("Previous session expired, finish all lingering activities")
            BroadcastReceiver.broadcast(this, Intent("session_expired"))
            showSessionExpiredDialog()
        }

        // this will happen when we need to authorize - the user will be sent here, but the
        // boot has already happened, so skip it. Otherwise, we'll end in a, infinite loop
        if(intent.getBooleanExtra("noboot", false)) {
            addFragmentOnTop(R.id.containerFl, UserCarouselComponentFragment(), false)
            return
        }

        addFragmentOnTop(R.id.containerFl, splashFragment, false)

        if(!ConnectionManager(this).isConnected())
        {
            AlertDialog.Builder(this)
                    .setTitle(Translation.error.noInternetTitle)
                    .setMessage(Translation.error.noInternetMessage)
                    .setPositiveButton(Translation.defaultSection.close) { dialog, which ->
                        finish()
                    }
                    .show()
            return
        }

        /*
        supportFragmentManager.addOnBackStackChangedListener {
            //Timber.e("bs changed entryCount ${supportFragmentManager.backStackEntryCount}")
            if (supportFragmentManager.backStackEntryCount == 0) {
                if (!isDestroyed)
                    finish()
            }
        }
        */

        CrashManager.register(this, object : CrashManagerListener() {
            override fun onNoCrashesFound() {
                super.onNoCrashesFound()
                Timber.d("No crashes found")
            }

            override fun onNewCrashesFound() {
                super.onNewCrashesFound()
                Timber.d("New crashes found")
            }

            override fun shouldAutoUploadCrashes(): Boolean {
                return true
            }
        })

        presenter.startup()
    }

    override fun performVersionControl() {
        //NStack.appOpen({ success -> Timber.e("appopen success = $success") })
        NStack.onAppUpdateListener = {
            Timber.e("ONAPPUPDATELISTENER")

            showUpdateDialog(it)
        }
        NStack.appOpen()
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
                        else -> {
                            dialog.dismiss()
                            presenter.proceed()
                        }
                    }
                }
                .setNegativeButton(appUpdate.negativeBtn) { dialog, which ->
                }
                .show()
    }

    private fun showSessionExpiredDialog() {
        AlertDialog.Builder(this)
                .setTitle(Translation.error.authenticationErrorTitle)
                .setMessage(Translation.error.authenticationErrorMessage)
                .setPositiveButton(Translation.defaultSection.ok) { dialog, which ->

                }
                .show()
    }

    override fun showUserCarouselComponent() {
        splashFragment?.transitionToUserCarouselFragment()
        //setRootFragment(R.id.containerFl, UserCarouselComponentFragment())
    }

    override fun showWelcomeComponent() {
        splashFragment?.transitionToWelcomeFragment()
    }

    fun continueFromDisclaimer()
    {
        setRootFragment(R.id.containerFl, WelcomeComponentFragment())
    }

    override fun showDisclaimer() {
        Timber.e("showing beta disclaimer")
        addFragmentOnTop(R.id.containerFl, disclaimerFragment, false)
    }

    override fun startMain() {
        //TODO change back to regular when done
        NavBarComponentFragment.currentMenuItem = R.id.actionMail
        startActivity(Intent(this, HomeActivity::class.java))

        //overridePendingTransition(0, 0)
        Timber.d("Finishing StartActivity")
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
        if (supportFragmentManager.findFragmentById(R.id.containerFl) is CompletedComponentFragment ){
            setRootFragment(R.id.containerFl, LoginComponentFragment())
            return
        }

        Timber.e("on back bs count ${supportFragmentManager.backStackEntryCount}")
        if (supportFragmentManager.backStackEntryCount > 1) {
            supportFragmentManager.popBackStack()
        } else {
            finishAfterTransition()
        }
    }

    override fun onPause() {
        super.onPause()
        if(BuildConfig.DEBUG)
            UpdateManager.unregister()
    }

    override fun onResume() {
        super.onResume()
        if(BuildConfig.DEBUG) {
            UpdateManager.register(this)
            debugConfEnvTv.text = "Conf/Env: ${Config.getCurrentConfigName()}/${Config.getCurrentEnvironmentName()}"
        }
    }

    override fun handleSessionExpired() {
        Timber.e("StartActivity ignoring session expired event")
    }

    override fun bootstrapDone() {
        updateConfEnvDisplay()
    }

    private fun updateConfEnvDisplay()
    {
        if(BuildConfig.DEBUG) {
            debugConfEnvTv.text = "Conf/Env: ${Config.getCurrentConfigName()}/${Config.getCurrentEnvironmentName()}"
            debugConfEnvTv.setVisible(true)
        }
    }
}
