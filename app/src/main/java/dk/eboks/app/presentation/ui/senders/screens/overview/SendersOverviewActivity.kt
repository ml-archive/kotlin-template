package dk.eboks.app.presentation.ui.senders.screens.overview

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dk.eboks.app.R
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.sender.CollectionContainer
import dk.eboks.app.domain.models.sender.Segment
import dk.eboks.app.domain.models.sender.Sender
import dk.eboks.app.presentation.base.BaseActivity
import dk.eboks.app.presentation.ui.senders.screens.browse.SearchSendersActivity
import dk.eboks.app.presentation.ui.senders.screens.detail.SenderDetailActivity
import dk.eboks.app.presentation.ui.senders.screens.registrations.PendingActivity
import dk.eboks.app.presentation.ui.senders.screens.registrations.RegistrationsActivity
import dk.eboks.app.presentation.ui.senders.screens.segment.SegmentDetailActivity
import kotlinx.android.synthetic.main.activity_senders_overview.*
import kotlinx.android.synthetic.main.include_toolbar.*
import javax.inject.Inject

class SendersOverviewActivity : BaseActivity(), SendersOverviewContract.View,
    SendersCollectionAdapter.Callback {
    @Inject
    lateinit var presenter: SendersOverviewContract.Presenter

    private lateinit var adapter: SendersCollectionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_senders_overview)
        component.inject(this)
        adapter = SendersCollectionAdapter(this)
        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recyclerView.adapter = adapter
        presenter.onViewCreated(this, lifecycle)
        setupTopBar()
        senderPendingBtn.setOnClickListener { v ->
            startActivity(Intent(v.context, PendingActivity::class.java))
        }
    }

    // TODO add translation
    private fun setupTopBar() {
        mainTb.navigationIcon = null
        mainTb.title = Translation.senders.title
        val menuRegist = mainTb.menu.add(Translation.senders.registrations)
        menuRegist.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
        menuRegist.setOnMenuItemClickListener { item: MenuItem ->
            startActivity(Intent(this, RegistrationsActivity::class.java))
            true
        }
        val menuSearch = mainTb.menu.add("search")
        menuSearch.setIcon(R.drawable.search)
        menuSearch.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        menuSearch.setOnMenuItemClickListener { item: MenuItem ->
            startActivity(Intent(this, SearchSendersActivity::class.java))
            true
        }
    }

    override fun onSenderClick(sender: Sender) {
        val i = Intent(this, SenderDetailActivity::class.java)
        i.putExtra(Sender::class.simpleName, sender)
        startActivity(i)
    }

    override fun onRegisterSenderClick(sender: Sender) {
        presenter.registerSender(sender)
    }

    override fun onUnregisterSenderClick(sender: Sender) {
        presenter.unregisterSender(sender)
    }

    override fun onSegmentClick(segment: Segment) {
        val i = Intent(this, SegmentDetailActivity::class.java)
        i.putExtra(Segment::class.simpleName, segment)
        startActivity(i)
    }

    override fun showCollections(collections: List<CollectionContainer>) {
        adapter.setData(collections)
    }

    override fun showSuccess() {}

    override fun showError(s: String) {}

    override fun hidePendingRegistrations() {
        senderPendingBtn.visibility = View.GONE
    }

    override fun showPendingRegistrations(pendingRegistrationsString: String) {
        senderPendingBtn.visibility = View.VISIBLE
        senderPendingBtn.text = pendingRegistrationsString
    }
}
