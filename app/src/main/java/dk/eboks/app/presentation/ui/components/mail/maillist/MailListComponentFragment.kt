package dk.eboks.app.presentation.ui.components.mail.maillist

import android.graphics.Typeface
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
import dk.eboks.app.domain.models.folder.Folder
import dk.eboks.app.domain.models.message.Message
import dk.eboks.app.domain.models.sender.Sender
import dk.eboks.app.presentation.base.BaseFragment
import dk.eboks.app.presentation.ui.screens.message.opening.MessageOpeningActivity
import dk.eboks.app.util.Starter
import kotlinx.android.synthetic.main.fragment_mail_list_component.*
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

        arguments?.let { args->
            if(args.containsKey("folder"))
            {
                val folder = args.getSerializable("folder") as Folder
                presenter.setup(folder)
            }
            if(args.containsKey("sender"))
            {
                val sender = args.getSerializable("sender") as Sender
                presenter.setup(sender)
            }
        }


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

    inner class MessageAdapter : RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

        inner class MessageViewHolder(val root : View) : RecyclerView.ViewHolder(root)
        {
            val circleIv = root.findViewById<ImageView>(R.id.circleIv)
            val titleTv = root.findViewById<TextView>(R.id.titleTv)
            val subTitleTv = root.findViewById<TextView>(R.id.subTitleTv)
            val urgentTv = root.findViewById<TextView>(R.id.urgentTv)
            val dateTv = root.findViewById<TextView>(R.id.dateTv)
            val clipIv = root.findViewById<ImageView>(R.id.clipIv)

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
            var currentItem = messages[position]

            if(currentItem.unread){
                holder?.titleTv?.setTypeface(null, Typeface.BOLD)
                holder?.dateTv?.setTypeface(null, Typeface.BOLD)
                holder?.subTitleTv?.setTypeface(null, Typeface.BOLD)
                holder?.dateTv?.setTextColor(resources.getColor(R.color.darkGreyBlue))
            } else {
                holder?.titleTv?.setTypeface(null, Typeface.NORMAL)
                holder?.dateTv?.setTypeface(null, Typeface.NORMAL)
                holder?.subTitleTv?.setTypeface(null, Typeface.NORMAL)
            }

            if(currentItem.sender != null) {
                holder?.circleIv?.let {
                    Glide.with(context)
                            .applyDefaultRequestOptions(RequestOptions().placeholder(R.drawable.icon_48_profile_grey))
                            .load(currentItem.sender?.logo?.url)
                            .into(it)
                    it.isSelected = currentItem.unread
                }
            }
            holder?.titleTv?.text = currentItem.sender?.name
            holder?.dateTv?.text = formatter.formatDateRelative(messages[position])
            holder?.subTitleTv?.text = currentItem.subject

            if(currentItem.status?.text != null){
                holder?.urgentTv?.visibility = View.VISIBLE
                holder?.urgentTv?.text = currentItem.status?.text
            } else {
                holder?.urgentTv?.visibility = View.GONE
            }

            if (currentItem.numberOfAttachments >0){
                holder?.clipIv?.visibility = View.VISIBLE
            } else {
                holder?.clipIv?.visibility = View.GONE
            }

            holder?.root?.setOnClickListener {
                activity.Starter()
                        .activity(MessageOpeningActivity::class.java)
                        .putExtra(Message::class.java.simpleName, messages[position])
                        .start()
            }

        }
    }

}