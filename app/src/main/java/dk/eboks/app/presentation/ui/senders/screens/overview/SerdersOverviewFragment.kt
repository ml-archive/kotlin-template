package dk.eboks.app.presentation.ui.senders.screens.overview

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import dk.eboks.app.R
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.sender.CollectionContainer
import dk.eboks.app.domain.models.sender.Segment
import dk.eboks.app.domain.models.sender.Sender
import dk.eboks.app.presentation.base.BaseFragment
import dk.eboks.app.presentation.ui.senders.screens.browse.SearchSendersActivity
import dk.eboks.app.presentation.ui.senders.screens.detail.SenderDetailActivity
import dk.eboks.app.presentation.ui.senders.screens.registrations.PendingActivity
import dk.eboks.app.presentation.ui.senders.screens.registrations.RegistrationsActivity
import dk.eboks.app.presentation.ui.senders.screens.segment.SegmentDetailActivity
import kotlinx.android.synthetic.main.fragment_senders_overview.*
import javax.inject.Inject

class SerdersOverviewFragment : BaseFragment(), SendersOverviewContract.View,
    SendersCollectionAdapter.Callback {

    @Inject lateinit var presenter: SendersOverviewContract.Presenter

    private lateinit var adapter: SendersCollectionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_senders_overview, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.senders, menu)
        menu.findItem(R.id.menu_registrations).title = Translation.senders.registrations
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_registrations -> {
                startActivity(Intent(context ?: return false, RegistrationsActivity::class.java))
                return true
            }
            R.id.menu_search -> {
                startActivity(Intent(context ?: return false, SearchSendersActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = SendersCollectionAdapter(this)
        recyclerView.adapter = adapter
        presenter.onViewCreated(this, lifecycle)
    }

    override fun onSenderClick(sender: Sender) {
        val i = Intent(context ?: return, SenderDetailActivity::class.java)
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
        val i = Intent(context ?: return, SegmentDetailActivity::class.java)
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

    override fun showPendingRegistrations(pendingRegistrations: List<CollectionContainer>) {
        senderPendingBtn.visibility = View.VISIBLE
        senderPendingBtn.text = Translation.senders.pendingRegistrations.replace(
            "[COUNT]",
            pendingRegistrations.size.toString()
        )
        senderPendingBtn.setOnClickListener { v ->
            startActivity(
                PendingActivity.createIntent(
                    v.context,
                    pendingRegistrations
                )
            )
        }
    }
}