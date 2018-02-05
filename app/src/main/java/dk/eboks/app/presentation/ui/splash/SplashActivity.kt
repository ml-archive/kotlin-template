package dk.eboks.app.presentation.ui.splash


import android.content.Intent
import android.os.Bundle
import android.util.Log
import dk.eboks.app.App
import dk.eboks.app.R
import dk.eboks.app.injection.components.DaggerPresentationComponent
import dk.eboks.app.injection.components.PresentationComponent
import dk.eboks.app.injection.modules.PresentationModule
import dk.eboks.app.presentation.base.BaseActivity
import dk.eboks.app.presentation.base.MainNavigationBaseActivity
import dk.eboks.app.presentation.ui.mail.MailOverviewActivity
import dk.eboks.app.presentation.ui.main.MainActivity
import dk.nodes.nstack.kotlin.NStack
import dk.nodes.nstack.kotlin.UpdateType
import timber.log.Timber
import javax.inject.Inject

class SplashActivity : BaseActivity(), SplashContract.View {
    val component: PresentationComponent by lazy {
        DaggerPresentationComponent.builder()
                .appComponent((application as App).appComponent)
                .presentationModule(PresentationModule())
                .build()
    }
    @Inject lateinit var presenter: SplashContract.Presenter

    override fun injectDependencies() {
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(dk.eboks.app.R.layout.activity_splash)

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
        MainNavigationBaseActivity.currentMenuItem = R.id.actionMail
        startActivity(Intent(this, MailOverviewActivity::class.java))
        finish()
        overridePendingTransition(0, 0)
    }
}
