package dk.eboks.app.presentation.ui.message.components.detail.header

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import dk.eboks.app.R
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.message.Message
import dk.eboks.app.domain.models.message.MessageType
import dk.eboks.app.presentation.base.BaseFragment
import dk.eboks.app.presentation.ui.message.screens.embedded.MessageEmbeddedActivity
import kotlinx.android.synthetic.main.fragment_header_component.*
import javax.inject.Inject

/**
 * Created by bison on 09-02-2018.
 */
class HeaderComponentFragment : BaseFragment(), HeaderComponentContract.View {
    @Inject
    lateinit var presenter: HeaderComponentContract.Presenter

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_header_component, container, false)
        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
        arguments?.let { args ->
            if (args.getBoolean("show_divider", false))
                dividerV.visibility = View.VISIBLE
        }
    }

    override fun updateView(message: Message) {
        setDrawerHeader(message)

        when (message.type) {
            MessageType.RECEIVED -> {
                senderTv.text = message.sender?.name ?: ""
                titleTv.text = message.subject
                message.sender?.logo.let {
                    Glide.with(context).load(it?.url).into(senderLogoIv)
                }
            }
            MessageType.DRAFT -> {
                senderTv.text = message.sender?.name ?: ""
                titleTv.text = message.subject
                senderLogoIv.visibility = View.GONE
            }
            MessageType.SENT -> {
                senderTv.text = "${Translation.message.recipientPrefixTo} ${message.sender?.name ?: ""}"
                titleTv.text = message.subject
                senderLogoIv.visibility = View.GONE
            }
            MessageType.UPLOAD -> {
                senderTv.text = Translation.message.uploadedByYou
                titleTv.text = message.subject
                message.sender?.logo.let {
                    senderLogoIv.setImageResource(R.drawable.ic_menu_uploads)
                }
            }
            else -> {
                senderTv.text = message.sender?.name ?: ""
                titleTv.text = message.subject
                message.sender?.logo.let {
                    Glide.with(context).load(it?.url).into(senderLogoIv)
                }
            }
        }
    }

    private fun setDrawerHeader(message: Message) {
        // only showing the ekstra row if its a MessageEmbeddedActivitiy
        ekstraInfoContainerLl.visibility = View.GONE

        if (activity is MessageEmbeddedActivity) {
            ekstraInfoContainerLl.visibility = View.VISIBLE

            if (message.numberOfAttachments > 0) {

                attachmentsTv.visibility = View.VISIBLE
                attachmentsIv.visibility = View.VISIBLE
                notesTv.visibility = View.VISIBLE
                notesTv.text = " " + Translation.message.note

                if (message.numberOfAttachments == 1) {
                    attachmentsTv.text = "" + message.numberOfAttachments + " " + Translation.message.numberOfAttachmentsSingularSuffix
                } else {
                    attachmentsTv.text = "" + message.numberOfAttachments + " " + Translation.message.numberOfAttachmentsPluralSuffix
                }
            } else {
                attachmentsTv.visibility = View.GONE
                attachmentsIv.visibility = View.GONE
                notesTv.visibility = View.GONE
            }
        }
    }
}