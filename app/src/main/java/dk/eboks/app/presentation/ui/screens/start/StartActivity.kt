package dk.eboks.app.presentation.ui.screens.start

import android.content.Intent
import android.os.Bundle
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
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
    }

    fun replaceFragment(fragment: BaseFragment?) {
        fragment?.let {
            supportFragmentManager.beginTransaction().replace(
                    R.id.containerFl,
                    it,
                    fragment.javaClass.simpleName
            ).addToBackStack(fragment.javaClass.simpleName).commit()
        }
    }

    fun addFragment(fragment: BaseFragment?) {
        fragment?.let {
            supportFragmentManager.beginTransaction().add(
                    R.id.containerFl,
                    it,
                    fragment.javaClass.simpleName
            ).addToBackStack(fragment.javaClass.simpleName).commit()
        }
    }

    fun emptyBackStack() {
        val trans = supportFragmentManager.beginTransaction()
        for (frag in supportFragmentManager.fragments) {
            trans.remove(frag)
            Timber.e("Removing frag")
        }
        trans.commitNowAllowingStateLoss()
    }

    override fun setupTranslations() {

    }

    override fun showError(msg: String) {
        Timber.e(msg) // errorhandling lol
    }

    override fun performVersionControl() {
        NStack.appOpen({ success -> Timber.e("appopen success = $success") })

        NStack.onAppUpdateListener = {
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
        showLogo(false)
        addFragment(UserCarouselComponentFragment())
    }

    override fun showWelcomeComponent() {
        showLogo(false)
        replaceFragment(WelcomeComponentFragment())
    }

    override fun startMain() {
        NavBarComponentFragment.currentMenuItem = R.id.actionMail
        startActivity(Intent(this, MailOverviewActivity::class.java))

        //overridePendingTransition(0, 0)
        finishAfterTransition()
    }

    override fun onBackPressed() {
        Timber.e("entryCount ${supportFragmentManager.backStackEntryCount}")
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
            supportFragmentManager.executePendingTransactions()
            if (supportFragmentManager.backStackEntryCount == 0)
                super.onBackPressed()
        } else
            super.onBackPressed()
    }

    fun showLogo(show: Boolean) {
        logoIv.visibility = if (show) View.VISIBLE else View.GONE
    }
}
