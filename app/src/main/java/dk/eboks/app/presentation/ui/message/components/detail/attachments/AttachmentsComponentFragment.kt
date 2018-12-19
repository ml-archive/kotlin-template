package dk.eboks.app.presentation.ui.message.components.detail.attachments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import dk.eboks.app.R
import dk.eboks.app.domain.managers.EboksFormatter
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.message.Content
import dk.eboks.app.domain.models.message.Message
import dk.eboks.app.presentation.base.BaseFragment
import dk.eboks.app.util.FileUtils
import kotlinx.android.synthetic.main.fragment_attachments_component.*
import javax.inject.Inject

/**
 * Created by bison on 09-02-2018.
 */
class AttachmentsComponentFragment : BaseFragment(), AttachmentsComponentContract.View {
    @Inject
    lateinit var presenter : AttachmentsComponentContract.Presenter

    @Inject
    lateinit var formatter : EboksFormatter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_attachments_component, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
    }

    override fun updateView(message: Message) {
        attachmentsLl.removeAllViews()
        val li: LayoutInflater = LayoutInflater.from(context)
        message.attachments?.let {
            if(it.isNotEmpty()) {
                for (attachment in it) {
                    val v = li.inflate(R.layout.viewholder_attachment, attachmentsLl, false)
                    v.findViewById<TextView>(R.id.nameTv)?.text = attachment.title
                    v.findViewById<TextView>(R.id.sizeTv)?.text = "${formatter.formatSize(attachment)}"
                    attachmentsLl.addView(v)
                    v.setOnClickListener {
                        presenter.openAttachment(attachment)
                    }
                }
            }
            else
                hide()
        }
        if(message.attachments == null)
        {
            hide()
        }
    }

    fun hide()
    {
        attachmentsTv.visibility = View.GONE
        attachmentsLl.visibility = View.GONE
    }

    override fun openExternalViewer(attachment: Content, filename: String, mimeType : String) {
        if(!FileUtils.openExternalViewer(context ?: return, filename, mimeType))
        {   // could not be opened in external viewer, ask if user wanna save to downloads
            AlertDialog.Builder(context?: return)
                    .setTitle(Translation.error.attachmentErrorTitle)
                    .setMessage(Translation.error.attachmentErrorMessage)
                    .setPositiveButton(Translation.error.attachmentErrorSaveBtn) { dialogInterface, i ->
                        presenter.saveAttachment(attachment)
                    }
                .setNegativeButton(Translation.error.attachmentErrorNegativeBtn) { dialogInterface, i ->

                }
                .show()
        }
    }
}