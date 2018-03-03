package dk.eboks.app.presentation.ui.screens.start

import android.content.Intent
import android.os.Bundle
import android.view.View
import dk.eboks.app.R
import dk.eboks.app.presentation.base.BaseActivity
import dk.eboks.app.presentation.base.BaseFragment
import dk.eboks.app.presentation.ui.components.navigation.NavBarComponentFragment
import dk.eboks.app.presentation.ui.components.start.welcome.WelcomeComponentFragment
import dk.eboks.app.presentation.ui.screens.mail.overview.MailOverviewActivity
import dk.nodes.nstack.kotlin.NStack
import dk.nodes.nstack.kotlin.UpdateType
import kotlinx.android.synthetic.main.activity_start.*
import timber.log.Timber
import javax.inject.Inject

class StartActivity : BaseActivity(), StartContract.View {
    @Inject lateinit var presenter: StartContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(dk.eboks.app.R.layout.activity_start)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
    }

    fun replaceFragment(fragment : BaseFragment?)
    {
        fragment?.let{
            supportFragmentManager.beginTransaction().replace(R.id.containerFl, it, fragment.javaClass.simpleName).addToBackStack(fragment.javaClass.simpleName).commit()
        }
    }

    fun addFragment(fragment : BaseFragment?)
    {
        fragment?.let{
            supportFragmentManager.beginTransaction().add(R.id.containerFl, it, fragment.javaClass.simpleName).addToBackStack(fragment.javaClass.simpleName).commit()
        }
    }

    override fun setupTranslations() {

    }

    override fun showError(msg: String) {
        Timber.e(msg) // errorhandling lol
    }

    override fun performVersionControl() {
        NStack.appOpen({ success -> Timber.e("appopen success = $success") })

        NStack.versionControl(this, { type, builder ->
            when (type) {
                UpdateType.UPDATE -> {
                    Timber.e("Nstack UPDATE")
                    builder?.setOnDismissListener {
                        presenter.proceed()
                    }
                    builder?.show()
                }
                UpdateType.FORCE_UPDATE -> {
                    Timber.e("Nstack FORCEUPDATE")
                    builder?.setOnDismissListener { finish() }
                    builder?.show()
                }
                else -> {
                    Timber.e("Nstack ELSE = ${type}")
                    presenter.proceed()
                }
            }
        })
    }

    override fun showWelcomeComponent()
    {
        addFragment(WelcomeComponentFragment())
    }

    override fun startMain() {
        NavBarComponentFragment.currentMenuItem = R.id.actionMail
        startActivity(Intent(this, MailOverviewActivity::class.java))

        //overridePendingTransition(0, 0)
        finishAfterTransition()
    }

    override fun onBackPressed() {
        Timber.e("entryCount ${supportFragmentManager.backStackEntryCount}")
        if(supportFragmentManager.backStackEntryCount > 0)
        {
            supportFragmentManager.popBackStack()
            supportFragmentManager.executePendingTransactions()
            if(supportFragmentManager.backStackEntryCount == 0)
                super.onBackPressed()
        }
        else
            super.onBackPressed()
    }

    fun showLogo(show : Boolean)
    {
        logoIv.visibility = if(show) View.VISIBLE else View.INVISIBLE
    }
}
