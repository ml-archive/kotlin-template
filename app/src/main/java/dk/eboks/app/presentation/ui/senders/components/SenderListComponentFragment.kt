package dk.eboks.app.presentation.ui.senders.components

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import dk.eboks.app.R
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.sender.CollectionContainer
import dk.eboks.app.domain.models.sender.Sender
import dk.eboks.app.presentation.base.BaseFragment
import dk.eboks.app.presentation.ui.senders.components.register.RegistrationContract
import dk.eboks.app.presentation.ui.senders.screens.detail.SenderDetailActivity
import kotlinx.android.synthetic.main.fragment_senders_component.*
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Christian on 3/20/2018.
 * @author Christian
 * @since 3/20/2018.
 */
class SenderListComponentFragment : BaseFragment(), RegistrationContract.View {

    // the sound of silence!
    override fun showSuccess() {}

    override fun showError(message: String) {}

    @Inject
    lateinit var presenter: RegistrationContract.Presenter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_senders_component, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        component.inject(this)
        presenter.onViewCreated(this, lifecycle)

        sendersListLl.removeAllViews()

        arguments?.getParcelable<CollectionContainer>(CollectionContainer::class.simpleName)?.let {
            sendersTitleTv.text = it.description

            it.senders?.forEach { sender ->
                val v = inflator.inflate(R.layout.viewholder_sender, sendersListLl, false)
                Timber.v("inflate sender: ${sender.name}, ${sender.logo?.url}")
                val senderNameTv = v.findViewById<TextView>(R.id.senderNameTv)
                val senderRegisterBtn = v.findViewById<Button>(R.id.senderRegisterBtn)
                val senderLogoIv = v.findViewById<ImageView>(R.id.senderLogoIv)

                Glide.with(context ?: return)
                    .load(sender.logo?.url)
                    .apply(
                        RequestOptions()
                            .fallback(R.drawable.icon_64_senders_private)
                            .placeholder(R.drawable.icon_64_senders_private)
                    )
                    .into(senderLogoIv)
                senderNameTv.text = sender.name
                senderRegisterBtn.visibility = View.VISIBLE
                setButtonText(senderRegisterBtn, sender)

                senderRegisterBtn.setOnClickListener { v ->
                    if (sender.registered != 0) {
                        AlertDialog.Builder(v.context)
                            .setTitle(Translation.senders.unregisterAlertTitle)
                            .setMessage(Translation.senders.unregisterAlertDescription)
                            .setNegativeButton(Translation.defaultSection.cancel) { dialog, which ->
                                dialog.cancel()
                            }
                            .setPositiveButton(Translation.defaultSection.ok) { dialog, which ->
                                presenter.unregisterSender(sender)
                                sender.registered = 0
                                setButtonText(senderRegisterBtn, sender)
                                dialog.dismiss()
                            }
                            .show()
                    } else {
                        AlertDialog.Builder(v.context)
                            .setTitle(Translation.senders.registerAlertTitle)
                            .setMessage(Translation.senders.registerAlertDescription)
                            .setNegativeButton(Translation.defaultSection.cancel) { dialog, which ->
                                dialog.cancel()
                            }
                            .setPositiveButton(Translation.defaultSection.ok) { dialog, which ->
                                presenter.registerSender(sender)
                                sender.registered = 1
                                setButtonText(senderRegisterBtn, sender)
                                dialog.dismiss()
                            }
                            .show()
                    }
                }
                v.setOnClickListener { _ ->
                    val i = Intent(context, SenderDetailActivity::class.java)
                    i.putExtra(Sender::class.simpleName, sender)
                    startActivity(i)
                }
                sendersListLl.addView(v)
            }
            sendersListLl.requestLayout()
        }
    }

    private fun setButtonText(textView: TextView, sender: Sender) {
        textView.text = when (sender.registered) {
            0 -> Translation.senders.register
            else -> Translation.senders.registered
        }
    }
}