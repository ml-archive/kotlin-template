package dk.eboks.app.presentation.ui.message

import android.content.Intent
import android.os.Bundle
import android.util.Log
import dk.eboks.app.injection.components.DaggerPresentationComponent
import dk.eboks.app.injection.components.PresentationComponent
import dk.eboks.app.injection.modules.PresentationModule
import dk.eboks.app.presentation.base.MainNavigationBaseActivity
import dk.eboks.app.presentation.ui.message.sheet.MessageSheetActivity
import dk.nodes.nstack.kotlin.NStack
import kotlinx.android.synthetic.main.activity_message.*
import javax.inject.Inject

class MessageActivity : MainNavigationBaseActivity(), MessageContract.View {
    val component: PresentationComponent by lazy {
        DaggerPresentationComponent.builder()
                .appComponent((application as dk.eboks.app.App).appComponent)
                .presentationModule(PresentationModule())
                .build()
    }
    @Inject lateinit var presenter: MessageContract.Presenter

    override fun injectDependencies() {
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(dk.eboks.app.R.layout.activity_message)
        circleIv.setOnClickListener {
            circleIv.isSelected = !circleIv.isSelected
        }
        startActivity(Intent(this, MessageSheetActivity::class.java))
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    override fun setupTranslations() {

    }

    override fun onResume() {
        super.onResume()
    }

    override fun showError(msg: String) {
        Log.e("debug", msg)
    }
}
