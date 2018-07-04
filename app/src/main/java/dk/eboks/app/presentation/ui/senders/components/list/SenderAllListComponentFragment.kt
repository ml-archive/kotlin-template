package dk.eboks.app.presentation.ui.senders.components.list

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import dk.eboks.app.R
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.sender.Sender
import dk.eboks.app.presentation.base.BaseFragment
import dk.eboks.app.presentation.ui.mail.screens.list.MailListActivity
import kotlinx.android.synthetic.main.activity_senders_list.*
import kotlinx.android.synthetic.main.fragment_sender_list.*
import javax.inject.Inject
import kotlinx.android.synthetic.main.include_toolbar.*

/**
 * Created by bison on 09-02-2018.
 */
class SenderAllListComponentFragment : BaseFragment(), SenderAllListComponentContract.View {

    @Inject
    lateinit var presenter: SenderAllListComponentContract.Presenter

    var senders: MutableList<Sender> = ArrayList()
    var filteredSenders: MutableList<Sender> = ArrayList()
    var searchMode: Boolean = false


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_sender_list, container, false)
        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)

        refreshSrl.setOnRefreshListener {
            presenter.refresh()
        }
        setupRecyclerView()
        setupTopBar()
        setupSearchBar()
    }

    private fun setupSearchBar() {

        getBaseActivity()?.searchAllSenderTb?.setNavigationIcon(R.drawable.icon_48_chevron_left_red_navigationbar)
        getBaseActivity()?.searchAllSenderTb?.setNavigationOnClickListener {
            switchMode()
        }

        activity?.searchAllSenderSv?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            var filterSenders = Runnable{
                var text = activity?.searchAllSenderSv?.query?.toString()?.trim() ?: ""
                presenter.searchSenders(text)

            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                activity?.searchAllSenderSv?.removeCallbacks(filterSenders)
                activity?.searchAllSenderSv?.post(filterSenders)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if(newText.isNullOrBlank()) {
                    filteredSenders.clear()
                    allSendersRv?.adapter?.notifyDataSetChanged()
                } else {
                    activity?.searchAllSenderSv?.removeCallbacks(filterSenders)
                    activity?.searchAllSenderSv?.postDelayed(filterSenders, 500)
                }
                return true
            }
        })
    }


    private fun setupTopBar() {
        getBaseActivity()?.mainTb?.menu?.clear()

        getBaseActivity()?.mainTb?.title = Translation.senders.allSenders
        getBaseActivity()?.mainTb?.setNavigationIcon(R.drawable.icon_48_chevron_left_red_navigationbar)
        getBaseActivity()?.mainTb?.setNavigationOnClickListener {
            getBaseActivity()?.mainTb?.setNavigationIcon(null)
            fragmentManager.popBackStack()
        }


        val menuProfile = getBaseActivity()?.mainTb?.menu?.add("_search")
        menuProfile?.setIcon(R.drawable.search)
        menuProfile?.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        menuProfile?.setOnMenuItemClickListener { item: MenuItem ->
            switchMode()
            true
        }
    }

    private fun switchMode() {

        searchMode = !searchMode
        if (searchMode) {
            getBaseActivity()?.mainAb?.visibility = View.GONE
            getBaseActivity()?.mainAllSenderAb?.visibility = View.VISIBLE

        } else {
            getBaseActivity()?.mainAb?.visibility = View.VISIBLE
            getBaseActivity()?.mainAllSenderAb?.visibility = View.GONE
            presenter.loadAllSenders()
            allSendersRv.adapter.notifyDataSetChanged()
        }
    }

    private fun setupRecyclerView() {
        allSendersRv.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        allSendersRv.adapter = SendersAdapter()

    }

    override fun showProgress(show: Boolean) {
        progressBarFl.visibility = if (show) View.VISIBLE else View.GONE
        refreshSrl.isRefreshing = show
    }

    override fun showEmpty(show: Boolean) {
        // currently no emptystate - it should not happend as the showAll openActionTv is not shown if you dont have any mail from senders
    }

    override fun showSenders(senders: List<Sender>) {
        this.senders.clear()
        filteredSenders.clear()
        this.senders.addAll(senders)
        filteredSenders.addAll(senders)
        allSendersRv.adapter.notifyDataSetChanged()
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
                    val i = Intent(context, MailListActivity::class.java)
                    i.putExtra("sender", currentItem)
                    startActivity(i)

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
            return filteredSenders.size
        }

        override fun onBindViewHolder(holder: SenderViewHolder?, position: Int) {
            var last = (position == filteredSenders.size)
            holder?.bind(filteredSenders[position], last)
        }
    }
}