package dk.eboks.app.presentation.ui.message.components.detail.sign

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dk.eboks.app.R
import dk.eboks.app.domain.models.APIConstants
import dk.eboks.app.domain.models.message.Message
import dk.eboks.app.presentation.base.BaseFragment
import dk.eboks.app.presentation.ui.message.screens.sign.SignActivity
import dk.eboks.app.util.Starter
import kotlinx.android.synthetic.main.fragment_sign_button_component.*
import javax.inject.Inject

/**
 * Created by bison on 09-02-2018.
 */
class SignButtonComponentFragment : BaseFragment(), SignButtonComponentContract.View {

    @Inject
    lateinit var presenter : SignButtonComponentContract.Presenter

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_sign_button_component, container, false)
        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
        arguments?.getParcelable<Message>(Message::class.java.simpleName)?.let { msg ->
            //msg.sign?.status?.type = 2
            configureButton(msg)
            signBtn.setOnClickListener {
                presenter.sign(msg)
            }
        }
    }

    private fun configureButton(msg : Message) {
        signBtn.isEnabled = false
        msg.sign?.let { sign->
            when(sign.status.type)
            {
                APIConstants.MSG_SIGN_WEB_ONLY -> {
                    signBtn.text = sign.status.title
                }
                APIConstants.MSG_SIGN_AVAILABLE -> {
                    signBtn.isEnabled = true
                    signBtn.text = sign.status.title
                }
                APIConstants.MSG_SIGN_SIGNED -> {
                    signBtn.text = sign.status.title
                }
                APIConstants.MSG_SIGN_OTHER -> {
                    signBtn.text = sign.status.title
                }
                else -> {
                    signBtn.text = sign.status.title
                }
            }
        }
    }

    override fun startSigning(msg: Message) {
        activity.Starter()
                .activity(SignActivity::class.java)
                .putExtra(Message::class.java.simpleName, msg)
                .start()

    }

}