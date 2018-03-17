package dk.eboks.app.presentation.ui.screens.start

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.view.View
import dk.eboks.app.R
import dk.eboks.app.presentation.base.BaseActivity
import dk.eboks.app.presentation.base.BaseFragment
import dk.eboks.app.presentation.ui.components.navigation.NavBarComponentFragment
import dk.eboks.app.presentation.ui.components.start.login.UserCarouselComponentFragment
import dk.eboks.app.presentation.ui.components.start.welcome.WelcomeComponentFragment
import dk.eboks.app.presentation.ui.screens.mail.overview.MailOverviewActivity
import dk.nodes.nstack.kotlin.NStack
import dk.nodes.nstack.kotlin.models.AppUpdate
import dk.nodes.nstack.kotlin.models.AppUpdateState
import kotlinx.android.synthetic.main.activity_start.*
import timber.log.Timber
import javax.inject.Inject

class StartActivity : BaseActivity(), StartContract.View {
    @Inject
    lateinit var presenter: StartContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(dk.eboks.app.R.layout.activity_start)
        // add dummy fragment to work around the last fragment is impossible to remove bug (tm)
        //supportFragmentManager.beginTransaction().add(R.id.containerFl, Fragment()).addToBackStack("dummy").commit()
        //supportFragmentManager.beginTransaction().add(R.id.containerFl, Fragment()).commitNow()
        //supportFragmentManager.executePendingTransactions()
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)

        supportFragmentManager.addOnBackStackChangedListener {
            Timber.e("bs changed entryCount ${supportFragmentManager.backStackEntryCount}")
            if (supportFragmentManager.backStackEntryCount == 0) {
                if(!isDestroyed)
                    finish()
            }
        }
        presenter.startup()
    }

    override fun setupTranslations() {

    }

    override fun showError(msg: String) {
        Timber.e(msg) // errorhandling lol
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
        Timber.e("showUserCarousel")
        showLogo(false)
        setRootFragment(R.id.containerFl, UserCarouselComponentFragment())
        //replaceFragment(R.id.containerFl, UserCarouselComponentFragment(), true)
    }

    override fun showWelcomeComponent() {
        Timber.e("showWelcome")
        showLogo(false)
        setRootFragment(R.id.containerFl, WelcomeComponentFragment())
        //replaceFragment(R.id.containerFl, WelcomeComponentFragment(), true)
    }

    override fun startMain() {
        NavBarComponentFragment.currentMenuItem = R.id.actionMail
        startActivity(Intent(this, MailOverviewActivity::class.java))

        //overridePendingTransition(0, 0)
        finishAfterTransition()
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        }
        else
            super.onBackPressed()
    }

    fun showLogo(show: Boolean) {
        logoIv.visibility = if (show) View.VISIBLE else View.GONE
    }
}
