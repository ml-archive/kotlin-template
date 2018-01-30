package dk.eboks.app.presentation.ui.mail

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.Menu
import android.view.View
import dk.eboks.app.R
import dk.eboks.app.injection.components.DaggerPresentationComponent
import dk.eboks.app.injection.components.PresentationComponent
import dk.eboks.app.injection.modules.PresentationModule
import dk.eboks.app.presentation.base.BaseActivity
import dk.eboks.app.presentation.base.MainNavigationBaseActivity
import dk.eboks.app.util.disableShiftingMode
import dk.nodes.nstack.kotlin.NStack
import dk.nodes.nstack.kotlin.UpdateType
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MailOverviewActivity : MainNavigationBaseActivity(), MailOverviewContract.View {
    val component: PresentationComponent by lazy {
        DaggerPresentationComponent.builder()
                .appComponent((application as dk.eboks.app.App).appComponent)
                .presentationModule(PresentationModule())
                .build()
    }
    @Inject lateinit var presenter: MailOverviewContract.Presenter

    override fun injectDependencies() {
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mail_overview)
        circleIv.setOnClickListener {
            circleIv.isSelected = !circleIv.isSelected
        }
    }

    override fun setupTranslations() {

    }

    override fun onResume() {
        super.onResume()
        NStack.translate(this@MailOverviewActivity)
    }

    override fun showError(msg: String) {
        Log.e("debug", msg)
    }
}
