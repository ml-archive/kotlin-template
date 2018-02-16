package dk.eboks.app.presentation.ui.components.mail.maillist

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import dk.eboks.app.R
import dk.eboks.app.domain.models.Attachment
import dk.eboks.app.domain.models.Message
import dk.eboks.app.domain.models.Sender
import dk.eboks.app.presentation.base.BaseFragment
import dk.eboks.app.presentation.ui.screens.debug.hinter.HintActivity
import dk.eboks.app.presentation.ui.screens.message.MessageActivity
import dk.eboks.app.presentation.ui.screens.message.sheet.MessageSheetActivity
import kotlinx.android.synthetic.main.fragment_mail_list_component.*
import timber.log.Timber
import java.util.*
import javax.inject.Inject

/**
 * Created by bison on 09-02-2018.
 */
class MailListComponentFragment : BaseFragment(), MailListComponentContract.View {

    @Inject
    lateinit var presenter : MailListComponentContract.Presenter

    var messages: MutableList<Message> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_mail_list_component, container, false)
        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
        setupRecyclerView()

        refreshSrl.setOnRefreshListener {
            presenter.refresh()
        }
    }

    override fun setupTranslations() {

    }

    override fun onShake() {
        if(showEmptyState)
        {
        }
        else
        {
        }
    }

    fun setupRecyclerView()
    {
        messagesRv.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        messagesRv.adapter = MessageAdapter()
    }

    override fun showRefreshProgress(show: Boolean) {
        refreshSrl.isRefreshing = show
    }

    override fun showMessages(messages: List<Message>) {
        this.messages.clear()
        this.messages.add(Message(0, "Police 42342342", false, Date(), Sender(0, "Alka Forsikring"), null, listOf(Attachment(1, "Sorteper.pdf", "13 KB"), Attachment(1, "HalvgrønneBent.pdf", "2,7 MB"), Attachment(1, "SvartaGudrun.pdf", "236 KB"), Attachment(1, "Lyserøde Lars.pdf", "1,2 MB"))))
        this.messages.add(Message(1, "Kontoudskrift", false, Date(), Sender(0, "Danske Bank"), null, listOf(Attachment(1, "Pels for begyndere.pdf", "13 KB"), Attachment(1, "alverdens_sten.pdf", "236 KB"))))
        this.messages.addAll(messages)
        messagesRv.adapter.notifyDataSetChanged()
    }

    override fun showError(msg: String) {
        Timber.e(msg)
    }


    inner class MessageAdapter : RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

        inner class MessageViewHolder(val root : View) : RecyclerView.ViewHolder(root)
        {
            val circleIv = root.findViewById<ImageView>(R.id.circleIv)
            val titleTv = root.findViewById<TextView>(R.id.titleTv)
            val subTitleTv = root.findViewById<TextView>(R.id.subTitleTv)
            val urgentTv = root.findViewById<TextView>(R.id.urgentTv)
            val dateTv = root.findViewById<TextView>(R.id.dateTv)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
            val v = LayoutInflater.from(context).inflate(R.layout.viewholder_message, parent, false)
            val vh = MessageViewHolder(v)
            return vh
        }

        override fun getItemCount(): Int {
            return messages.size
        }

        override fun onBindViewHolder(holder: MessageViewHolder?, position: Int) {
            if(messages[position].sender != null) {
                holder?.circleIv?.let {

                    Glide.with(context).load(messages[position].sender?.logo).into(it)
                    it.isSelected = messages[position].unread
                }
            }
            holder?.titleTv?.text = messages[position].sender?.name
            holder?.subTitleTv?.text = messages[position].name
            holder?.root?.setOnClickListener {
                Timber.e("supposed to launch")
                presenter.setCurrentMessage(messages[position])
                startActivity(Intent(context, MessageSheetActivity::class.java))
            }
            holder?.root?.setOnLongClickListener {
                Timber.e("supposed to launch")
                presenter.setCurrentMessage(messages[position])
                startActivity(Intent(context, MessageActivity::class.java))
                true
            }
        }
    }

}