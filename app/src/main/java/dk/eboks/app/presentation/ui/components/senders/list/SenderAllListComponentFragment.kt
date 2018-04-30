package dk.eboks.app.presentation.ui.components.senders.list

import android.graphics.Typeface
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import dk.eboks.app.R
import dk.eboks.app.domain.models.Image
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.folder.FolderType
import dk.eboks.app.domain.models.message.Message
import dk.eboks.app.domain.models.message.Messages
import dk.eboks.app.domain.models.protocol.Metadata
import dk.eboks.app.domain.models.sender.Sender
import dk.eboks.app.presentation.base.BaseFragment
import dk.eboks.app.presentation.ui.screens.message.opening.MessageOpeningActivity
import dk.eboks.app.util.Starter
import kotlinx.android.synthetic.main.fragment_sender_list.*
import kotlinx.android.synthetic.main.include_toolbar.*
import javax.inject.Inject

/**
 * Created by bison on 09-02-2018.
 */
class SenderAllListComponentFragment : BaseFragment(), SenderAllListComponentContract.View {

    @Inject
    lateinit var presenter: SenderAllListComponentContract.Presenter

    var senders: MutableList<Sender> = ArrayList()


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_sender_list, container, false)
        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)

        refreshSrl.setOnRefreshListener {
           //todo onrefresh
            // presenter.refresh()
        }
        setupRecyclerView()
        createMocks()
        setupTopBar()
    }

    private fun setupTopBar() {
        getBaseActivity()?.mainTb?.menu?.clear()

        getBaseActivity()?.mainTb?.title = Translation.senders.allSenders
        getBaseActivity()?.mainTb?.setNavigationIcon(R.drawable.icon_48_chevron_left_red_navigationbar)
        getBaseActivity()?.mainTb?.setNavigationOnClickListener {
            getBaseActivity()?.mainTb?.setNavigationIcon(null)
            fragmentManager.popBackStack()
        }

    }

    private fun createMocks() {
        var items: List<Message> = arrayListOf()
        senders.add(Sender(1, "name1", null, null, 0, Image("https://picsum.photos/200/?random"), null, null, Messages(items, Metadata(5, 5))))
        senders.add(Sender(1, "name2", null, null, 0, Image("https://picsum.photos/200/?random"), null, null, Messages(items, Metadata(5, 0))))
        senders.add(Sender(1, "name3", null, null, 0, Image("https://picsum.photos/200/?random"), null, null, Messages(items, Metadata(5, 3))))
        senders.add(Sender(1, "name4", null, null, 0, Image("https://picsum.photos/200/?random"), null, null, Messages(items, Metadata(5, 1))))
        senders.add(Sender(1, "name5", null, null, 0, Image("https://picsum.photos/200/?random"), null, null, Messages(items, Metadata(5, 0))))
        senders.add(Sender(1, "name6", null, null, 0, Image("https://picsum.photos/200/?random"), null, null, Messages(items, Metadata(5, 0))))
        senders.add(Sender(1, "name1", null, null, 0, Image("https://picsum.photos/200/?random"), null, null, Messages(items, Metadata(5, 5))))
        senders.add(Sender(1, "name2", null, null, 0, Image("https://picsum.photos/200/?random"), null, null, Messages(items, Metadata(5, 0))))
        senders.add(Sender(1, "name3", null, null, 0, Image("https://picsum.photos/200/?random"), null, null, Messages(items, Metadata(5, 3))))
        senders.add(Sender(1, "name4", null, null, 0, Image("https://picsum.photos/200/?random"), null, null, Messages(items, Metadata(5, 1))))
        senders.add(Sender(1, "name5", null, null, 0, Image("https://picsum.photos/200/?random"), null, null, Messages(items, Metadata(5, 0))))
        senders.add(Sender(1, "name6", null, null, 0, Image("https://picsum.photos/200/?random"), null, null, Messages(items, Metadata(5, 0))))
        senders.add(Sender(1, "name1", null, null, 0, Image("https://picsum.photos/200/?random"), null, null, Messages(items, Metadata(5, 5))))
        senders.add(Sender(1, "name2", null, null, 0, Image("https://picsum.photos/200/?random"), null, null, Messages(items, Metadata(5, 0))))
        senders.add(Sender(1, "name3", null, null, 0, Image("https://picsum.photos/200/?random"), null, null, Messages(items, Metadata(5, 3))))
        senders.add(Sender(1, "name4", null, null, 0, Image("https://picsum.photos/200/?random"), null, null, Messages(items, Metadata(5, 1))))
        senders.add(Sender(1, "name5", null, null, 0, Image("https://picsum.photos/200/?random"), null, null, Messages(items, Metadata(5, 0))))
        senders.add(Sender(1, "name6", null, null, 0, Image("https://picsum.photos/200/?random"), null, null, Messages(items, Metadata(5, 0))))
        sendersRv.adapter.notifyDataSetChanged()
    }

    private fun setupRecyclerView() {
        sendersRv.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        sendersRv.adapter = SendersAdapter()

    }


    inner class SendersAdapter : RecyclerView.Adapter<SendersAdapter.SenderViewHolder>() {

        inner class SenderViewHolder(val root: View) : RecyclerView.ViewHolder(root) {
            val title = root.findViewById<TextView>(R.id.titleTv)
            val unreadCountTv = root.findViewById<TextView>(R.id.unreadCountTv)
            val dividerV = root.findViewById<View>(R.id.dividerV)
            val circleIv = root.findViewById<ImageView>(R.id.circleIv)


            fun bind(currentItem: Sender, last: Boolean) {

                if (last) {
                    dividerV.visibility = View.GONE
                }

                title.text = currentItem.name

                currentItem?.logo?.url.let {
                    Glide.with(context)
                            .load(it)
                            .into(circleIv)
                }

                currentItem?.messages?.metadata?.unreadCount?.let { unreadCount ->

                    circleIv.isSelected = (unreadCount > 0)
                    if (unreadCount > 0) {
                        unreadCountTv.text = unreadCount.toString()
                        unreadCountTv.visibility = View.VISIBLE
                    } else {
                        unreadCountTv.visibility = View.GONE
                    }
                }

                val senderListener = View.OnClickListener {
                    //todo item clicked  start something ?

                }
                root.setOnClickListener(senderListener)
            }
        }


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SenderViewHolder {
            val v = LayoutInflater.from(context).inflate(R.layout.viewholder_senders_list_item, parent, false)
            val vh = SenderViewHolder(v)
            return vh
        }

        override fun getItemCount(): Int {
            return senders.size
        }

        override fun onBindViewHolder(holder: SenderViewHolder?, position: Int) {
            var last = (position == senders.size)
            holder?.bind(senders[position], last)
        }
    }
}