package dk.eboks.app.presentation.ui.screens.splash


import android.content.Intent
import android.os.Bundle
import android.util.Log
import dk.eboks.app.R
import dk.eboks.app.presentation.base.BaseActivity
import dk.eboks.app.presentation.ui.components.navigation.NavBarComponentFragment
import dk.eboks.app.presentation.ui.screens.mail.overview.MailOverviewActivity
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.nstack.kotlin.NStack
import dk.nodes.nstack.kotlin.UpdateType
import kotlinx.android.synthetic.main.activity_splash.*
import timber.log.Timber
import javax.inject.Inject

class SplashActivity : BaseActivity(), SplashContract.View {
    @Inject lateinit var presenter: SplashContract.Presenter
    @Inject lateinit var executor: Executor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(dk.eboks.app.R.layout.activity_splash)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
    }


    override fun setupTranslations() {

    }


    override fun showError(msg: String) {
        Log.e("debug", msg)
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

    override fun startMain() {
        NavBarComponentFragment.currentMenuItem = R.id.actionMail
        startActivity(Intent(this, MailOverviewActivity::class.java))
        finish()
        overridePendingTransition(0, 0)
    }
}
