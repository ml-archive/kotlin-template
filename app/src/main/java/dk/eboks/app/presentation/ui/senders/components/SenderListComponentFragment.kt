package dk.eboks.app.presentation.ui.senders.components

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
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
 * @author   Christian
 * @since    3/20/2018.
 */
class SenderListComponentFragment : BaseFragment(), RegistrationContract.View {

    // the sound of silence!
    override fun showSuccess() {}
    override fun showError(message: String) {}

    @Inject
    lateinit var presenter: RegistrationContract.Presenter

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_senders_component, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        component.inject(this)
        presenter.onViewCreated(this, lifecycle)

        sendersListLl.removeAllViews()

        val senders = arguments.getSerializable(CollectionContainer::class.simpleName) as CollectionContainer?
        senders?.let {
            sendersTitleTv.text = it.description?.title

            it.senders?.forEach {
                val v = inflator.inflate(R.layout.viewholder_sender, sendersListLl, false)
                Timber.v("inflate sender: ${it.name}, ${it.logo?.url}")
                val senderNameTv = v.findViewById<TextView>(R.id.senderNameTv)
                val senderRegisterBtn = v.findViewById<Button>(R.id.senderRegisterBtn)
                val senderLogoIv = v.findViewById<ImageView>(R.id.senderLogoIv)

                Glide.with(context)
                        .load(it.logo?.url)
                        .apply(RequestOptions()
                                .fallback(R.drawable.icon_64_senders_private)
                                .placeholder(R.drawable.icon_64_senders_private)
                        )
                        .into(senderLogoIv)
                senderNameTv.text = it.name
                senderRegisterBtn.visibility = View.VISIBLE
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
                v.setOnClickListener { _ ->
                    val i = Intent(context, SenderDetailActivity::class.java)
                    i.putExtra(Sender::class.simpleName, it)
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