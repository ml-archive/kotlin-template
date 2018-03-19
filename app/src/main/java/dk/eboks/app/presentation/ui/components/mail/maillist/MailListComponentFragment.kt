package dk.eboks.app.presentation.ui.components.mail.maillist

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import dk.eboks.app.R
import dk.eboks.app.domain.managers.EboksFormatter
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.message.Message
import dk.eboks.app.presentation.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_mail_list_component.*
import kotlinx.android.synthetic.main.viewholder_circular_sender.*
import timber.log.Timber
import java.util.*
import javax.inject.Inject

/**
 * Created by bison on 09-02-2018.
 */
class MailListComponentFragment : BaseFragment(), MailListComponentContract.View {

    @Inject
    lateinit var presenter : MailListComponentContract.Presenter
    @Inject
    lateinit var formatter: EboksFormatter

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
            noMessagesTv.text = Translation.mail.noMessagesToDisplay
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

    override fun showProgress(show: Boolean) {
        progressFl.visibility = if(show) View.VISIBLE else View.GONE
    }

    override fun showEmpty(show: Boolean) {
        emptyFl.visibility = if(show) View.VISIBLE else View.GONE
        contentFl.visibility = if(!show) View.VISIBLE else View.GONE
    }

    override fun showMessages(messages: List<Message>) {
        this.messages.clear()
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
                    Glide.with(context)
                            .applyDefaultRequestOptions(RequestOptions().placeholder(R.drawable.icon_48_profile_grey))
                            .load(messages[position].sender?.logo)
                            .into(it)
                    it.isSelected = messages[position].unread
                }
            }
            holder?.titleTv?.text = messages[position].sender?.name
            holder?.subTitleTv?.text = messages[position].name

            holder?.root?.setOnClickListener {
                presenter.openMessage(messages[position])
            }
            holder?.dateTv?.text = formatter.formatDateRelative(messages[position])
        }
    }

}