package dk.eboks.app.presentation.ui.screens.senders.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dk.eboks.app.R
import dk.eboks.app.domain.models.sender.Sender
import dk.eboks.app.presentation.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_sender_detail_information.*

/**
 * Created by Christian on 3/15/2018.
 * @author   Christian
 * @since    3/15/2018.
 */
class SenderDetailInfoFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_sender_detail_information, container, false)
        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        senderInfoMoreBtn.visibility = View.GONE
        senderInfoAdressLL.visibility = View.GONE
        senderInfoWebLL.visibility = View.GONE
        senderInfoPhoneLL.visibility = View.GONE
    }

    override fun setupTranslations() {
    }

    override fun setArguments(args: Bundle?) {
        super.setArguments(args)

        val sender = arguments.getSerializable(Sender::class.simpleName) as Sender?

        sender?.description?.let {
            senderInfoBodyTv.text = it.text
            it.link?.let {
                senderInfoMoreBtn.setOnClickListener { v ->
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(it.url)
                    if (intent.resolveActivity(context.packageManager) != null) {
                        startActivity(intent)
                    }
                }
                senderInfoMoreBtn.text = it.text
                senderInfoMoreBtn.visibility = View.VISIBLE
            }
        }

        sender?.address?.let {
            val s = StringBuilder().appendln(it.name).appendln(it.addressLine1)
            if (!it.addressLine2.isNullOrBlank()) {
                s.appendln(it.addressLine2)
            }
            s.append(it.zipCode).append(" ").appendln(it.city)

            val navString = "geo:0,0?q=${it.name},+${it.addressLine1},+${it.addressLine2},+${it.city},+${it.zipCode}"
            senderInfoAddressTv.text = s.toString()
            senderInfoAdressLL.setOnClickListener {
                val gmmIntentUri = Uri.parse(navString)
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                mapIntent.`package` = "com.google.android.apps.maps"
                if (mapIntent.resolveActivity(context.packageManager) != null) {
                    startActivity(mapIntent)
                }
            }
            senderInfoAdressLL.visibility = View.VISIBLE
        }
        sender?.address?.link?.let {
            val url = it.url

            senderInfoWebTv.text = url
            senderInfoWebLL.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(url)
                if (intent.resolveActivity(context.packageManager) != null) {
                    startActivity(intent)
                }
            }
            senderInfoWebLL.visibility = View.VISIBLE
        }
        sender?.address?.phone?.let {
            val phone = it
            senderInfoPhoneTv.text = phone
            senderInfoPhoneLL.setOnClickListener {
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:$phone")
                if (intent.resolveActivity(context.packageManager) != null) {
                    startActivity(intent)
                }
            }
            senderInfoPhoneLL.visibility = View.VISIBLE
        }
    }
}