package dk.eboks.app.presentation.ui.senders.screens.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dk.eboks.app.R
import dk.eboks.app.domain.models.sender.Sender
import dk.eboks.app.domain.models.shared.Address
import dk.eboks.app.domain.models.shared.Description
import dk.eboks.app.domain.models.shared.Link
import dk.eboks.app.presentation.base.BaseFragment
import dk.eboks.app.util.guard
import dk.eboks.app.util.visible
import kotlinx.android.synthetic.main.fragment_sender_detail_information.*
import timber.log.Timber

/**
 * Created by Christian on 3/15/2018.
 * @author Christian
 * @since 3/15/2018.
 */
class SenderDetailInfoFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sender_detail_information, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        senderInfoAdressLL.visibility = View.GONE
        senderInfoWebLL.visibility = View.GONE
        senderInfoPhoneLL.visibility = View.GONE
    }

    override fun setArguments(args: Bundle?) {
        super.setArguments(args)

        val sender = arguments?.getParcelable<Sender>(Sender::class.simpleName)
        sender?.description?.let {
            setDescription(it)
        }

        sender?.address?.let {
            setSenderAddress(it)
        }.guard {
            senderInfoAdressLL.visible = false
        }

        setLink(sender?.address?.link)
        setPhone(sender?.address?.phone)
    }

    private fun setSenderAddress(address: Address) {

        val city = StringBuilder()
                .appendNotEmpty(address.zipCode, false)
                .append(" ")
                .appendNotEmpty(address.city, false)

        val s = StringBuilder()
                .appendNotEmpty(address.name)
                .appendNotEmpty(address.addressLine1)
                .appendNotEmpty(address.addressLine2)
                .appendNotEmpty(city.toString())

        Timber.d("Address: $s")
        val navString = "geo:0,0?q=${address.name},+${address.addressLine1},+${address.addressLine2},+${address.city},+${address.zipCode}"
        senderInfoAddressTv.text = s.toString()
        senderInfoAdressLL.setOnClickListener {
            val gmmIntentUri = Uri.parse(navString)
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.`package` = "com.google.android.apps.maps"
            val context = context ?: return@setOnClickListener
            if (mapIntent.resolveActivity(context.packageManager) != null) {
                startActivity(mapIntent)
            }
        }
        senderInfoAdressLL.visibility = View.VISIBLE
    }

    private fun setPhone(phone: String?) {
        if (!phone.isNullOrBlank()) {
            senderInfoPhoneTv.text = phone
            senderInfoPhoneLL.setOnClickListener {
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:$phone")
                val context = context ?: return@setOnClickListener
                if (intent.resolveActivity(context.packageManager) != null) {
                    startActivity(intent)
                }
            }
            senderInfoPhoneLL.visibility = View.VISIBLE
        } else {
            senderInfoPhoneLL.visibility = View.GONE
        }
    }

    private fun setLink(link: Link?) {
        if (link != null && link.url.isNotEmpty()) {
            val url = link.url

            senderInfoWebTv.text = url
            senderInfoWebLL.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(url)
                val context = context ?: return@setOnClickListener
                if (intent.resolveActivity(context.packageManager) != null) {
                    startActivity(intent)
                }
            }
            senderInfoWebLL.visibility = View.VISIBLE
        } else {
            senderInfoWebLL.visibility = View.GONE
        }
    }

    private fun setDescription(description: Description) {
        senderInfoBodyTv.text = description.text
        description.link?.let {
            senderInfoMoreBtn.setOnClickListener { v ->
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(it.url)

                if (intent.resolveActivity(v.context.packageManager) != null) {
                    startActivity(intent)
                }
            }
        }
    }

    private fun StringBuilder.appendNotEmpty(string: String?, newLine: Boolean = true): StringBuilder {
        if (!string.isNullOrBlank()) {
            if (newLine) this.appendln(string) else this.append(string)
        }
        return this
    }
}