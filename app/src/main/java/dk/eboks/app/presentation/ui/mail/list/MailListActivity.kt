package dk.eboks.app.presentation.ui.mail.list

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import dk.eboks.app.R
import dk.eboks.app.domain.models.Message
import dk.eboks.app.domain.models.Sender
import dk.eboks.app.injection.components.DaggerPresentationComponent
import dk.eboks.app.injection.components.PresentationComponent
import dk.eboks.app.injection.modules.PresentationModule
import dk.eboks.app.presentation.base.MainNavigationBaseActivity
import dk.nodes.nstack.kotlin.NStack
import kotlinx.android.synthetic.main.activity_mail_list.*
import kotlinx.android.synthetic.main.include_toolnar.*
import java.util.*
import javax.inject.Inject

class MailListActivity : MainNavigationBaseActivity(), MailListContract.View {
    val component: PresentationComponent by lazy {
        DaggerPresentationComponent.builder()
                .appComponent((application as dk.eboks.app.App).appComponent)
                .presentationModule(PresentationModule())
                .build()
    }

    @Inject lateinit var presenter: MailListContract.Presenter

    var messages: MutableList<Message> = ArrayList()

    override fun injectDependencies() {
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mail_list)
        setupRecyclerView()

        refreshSrl.setOnRefreshListener {
            presenter.refresh()
        }
    }

    override fun onResume() {
        super.onResume()
        NStack.translate(this@MailListActivity)
    }

    override fun onShake() {
        if(showEmptyState)
        {
        }
        else
        {
        }
    }

    override fun setupTranslations() {
        toolbarTv.visibility = View.GONE
        toolbarLargeTv.visibility = View.VISIBLE
        toolbarLargeTv.text = "_All mail"
    }

    fun setupRecyclerView()
    {
        messagesRv.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        messagesRv.adapter = MessageAdapter()

        messages.add(Message(0, "Police 42342342", false, Date(), Sender(0, "Alka Forsikring"), null))
        messages.add(Message(1, "Kontoudskrift", false, Date(), Sender(0, "Danske Bank"), null))
    }


    override fun showError(msg: String) {
        Log.e("debug", msg)
    }


    override fun showMessages(messages: List<Message>) {
        this.messages.addAll(messages)
        messagesRv.adapter.notifyDataSetChanged()
    }
    /*
    override fun showSenders(messages: List<Sender>) {
        this.messages.addAll(messages)
        sendersRv.adapter.notifyDataSetChanged()
    }
    */


    override fun showRefreshProgress(show: Boolean) {
            refreshSrl.isRefreshing = show
    }


    inner class MessageAdapter : RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

        inner class MessageViewHolder(root : View) : RecyclerView.ViewHolder(root)
        {
            val circleIv = root.findViewById<ImageView>(R.id.circleIv)
            val titleTv = root.findViewById<TextView>(R.id.titleTv)
            val subTitleTv = root.findViewById<TextView>(R.id.subTitleTv)
            val urgentTv = root.findViewById<TextView>(R.id.urgentTv)
            val dateTv = root.findViewById<TextView>(R.id.dateTv)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
            val v = LayoutInflater.from(this@MailListActivity).inflate(R.layout.viewholder_message, parent, false)
            val vh = MessageViewHolder(v)
            return vh
        }

        override fun getItemCount(): Int {
            return messages.size
        }

        override fun onBindViewHolder(holder: MessageViewHolder?, position: Int) {
            if(messages[position].sender != null) {
                holder?.circleIv?.let {

                    Glide.with(this@MailListActivity).load(messages[position].sender?.imageUrl).into(it)
                    it.isSelected = !messages[position].isRead
                }
            }
            holder?.titleTv?.text = messages[position].sender?.name
            holder?.subTitleTv?.text = messages[position].name
        }
    }
}
