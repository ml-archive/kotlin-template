package dk.eboks.app.presentation.ui.components.senders

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import dk.eboks.app.R
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.sender.Sender
import dk.eboks.app.presentation.base.BaseFragment
import dk.eboks.app.presentation.ui.components.senders.register.RegistrationContract
import dk.eboks.app.presentation.ui.screens.senders.detail.SenderDetailActivity
import kotlinx.android.synthetic.main.fragment_sender_component.*
import javax.inject.Inject

/**
 * Created by Christian on 3/20/2018.
 * @author   Christian
 * @since    3/20/2018.
 */
class SenderComponentFragment : BaseFragment(), RegistrationContract.View {

    // the sound of silence!
    override fun showSuccess() {}
    override fun showError(message: String) {}

    @Inject
    lateinit var presenter: RegistrationContract.Presenter

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_sender_component, container, false)
    }


    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        component.inject(this)
        presenter.onViewCreated(this, lifecycle)

        val sender = arguments.getSerializable(Sender::class.simpleName) as Sender?
        sender?.let {
            Glide.with(context).load(sender.logo?.url).into(senderIv)
            senderNameTv.text = it.name
            senderCv.setOnClickListener {
                val i = Intent(context, SenderDetailActivity::class.java)
                i.putExtra(Sender::class.simpleName, sender)
                startActivity(i)
            }

            setButtonText(senderRegisterBtn, it)
            senderRegisterBtn.setOnClickListener { v ->
                if (it.registered != 0) {
                    AlertDialog.Builder(context)
                            .setTitle(Translation.senders.unregisterAlertTitle)
                            .setMessage(Translation.senders.unregisterAlertDescription)
                            .setNegativeButton(Translation.defaultSection.cancel) { dialog, which ->
                                dialog.cancel()
                            }
                            .setPositiveButton(Translation.defaultSection.ok) { dialog, which ->
                                presenter.unregisterSender(it)
                                it.registered = 0
                                setButtonText(senderRegisterBtn, it)
                                dialog.dismiss()
                            }
                            .show()
                } else {
                    AlertDialog.Builder(context)
                            .setTitle(Translation.senders.registerAlertTitle)
                            .setMessage(Translation.senders.registerAlertDescription)
                            .setNegativeButton(Translation.defaultSection.cancel) { dialog, which ->
                                dialog.cancel()
                            }
                            .setPositiveButton(Translation.defaultSection.ok) { dialog, which ->
                                presenter.registerSender(it)
                                it.registered = 1
                                setButtonText(senderRegisterBtn, it)
                                dialog.dismiss()
                            }
                            .show()
                }
            }
        }
    }

    private fun setButtonText(textView: TextView, sender: Sender) {
        textView.text = when (sender.registered) {
            0 -> Translation.senders.register
            else -> Translation.senders.registered
        }
    }
}