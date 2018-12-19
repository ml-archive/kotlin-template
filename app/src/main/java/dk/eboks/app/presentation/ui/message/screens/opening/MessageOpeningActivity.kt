package dk.eboks.app.presentation.ui.message.screens.opening

import android.os.Bundle
import android.view.View
import dk.eboks.app.R
import dk.eboks.app.domain.models.message.Message
import dk.eboks.app.presentation.base.BaseActivity
import dk.eboks.app.presentation.base.BaseFragment
import dk.eboks.app.presentation.ui.message.components.opening.protectedmessage.ProtectedMessageComponentFragment
import dk.eboks.app.util.guard
import dk.eboks.app.util.putArg
import kotlinx.android.synthetic.main.activity_message_opening.*
import javax.inject.Inject

class MessageOpeningActivity : BaseActivity(), MessageOpeningContract.View {
    @Inject lateinit var presenter: MessageOpeningContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(dk.eboks.app.R.layout.activity_message_opening)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
        intent?.extras?.getParcelable<Message>(Message::class.java.simpleName)?.let(presenter::setup)
            .guard { finish() }
    }

    override fun setOpeningFragment(cls: Class<out BaseFragment>, voluntaryReceipt : Boolean) {
        val fragment = cls.newInstance()
        progressPb.visibility = View.GONE
        fragment.putArg("voluntaryReceipt", voluntaryReceipt)
        fragment.let{
            supportFragmentManager.beginTransaction().add(R.id.contentFl, it, it::class.java.simpleName).commit()
        }
    }

    override fun showMessageLocked(loginProviderId: String, msg : Message) {
        val fragment = ProtectedMessageComponentFragment()
        progressPb.visibility = View.GONE
        fragment.putArg("loginProviderId", loginProviderId)
        fragment.putArg(Message::class.java.simpleName, msg)
        fragment.let{
            supportFragmentManager.beginTransaction().add(R.id.contentFl, it, it::class.java.simpleName).commit()
        }
    }

    override fun onBackPressed() {
        presenter.signalMessageOpenDone()
        super.onBackPressed()
    }

    override fun openMessage(msg: Message) {
        presenter.setup(msg)
    }

    override fun getNavigationMenuAction(): Int { return R.id.actionMail }
}
