package dk.eboks.app.presentation.ui.screens.message.opening

import android.os.Bundle
import dk.eboks.app.R
import dk.eboks.app.presentation.base.BaseActivity
import dk.eboks.app.presentation.ui.components.message.opening.privatesender.PrivateSenderWarningComponentFragment
import timber.log.Timber
import javax.inject.Inject

class MessageOpeningActivity : BaseActivity(), MessageOpeningContract.View {
    @Inject lateinit var presenter: MessageOpeningContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(dk.eboks.app.R.layout.activity_message_opening)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
    }

    override fun showError(msg: String) {
        Timber.e(msg) // errorhandling lol
    }

    override fun setPrivateSenderWarningFragment() {
        val fragment = PrivateSenderWarningComponentFragment()
        fragment?.let{
            supportFragmentManager.beginTransaction().add(R.id.contentFl, it, PrivateSenderWarningComponentFragment::class.java.simpleName).commit()
        }
    }

    override fun onBackPressed() {
        presenter.signalMessageOpenDone()
        super.onBackPressed()
    }

    override fun getNavigationMenuAction(): Int { return R.id.actionMail }
}
