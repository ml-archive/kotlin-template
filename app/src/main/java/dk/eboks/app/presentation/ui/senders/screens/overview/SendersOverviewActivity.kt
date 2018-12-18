package dk.eboks.app.presentation.ui.senders.screens.overview

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import dk.eboks.app.R
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.sender.CollectionContainer
import dk.eboks.app.domain.models.sender.Sender
import dk.eboks.app.presentation.base.BaseActivity
import dk.eboks.app.presentation.base.BaseFragment
import dk.eboks.app.presentation.ui.senders.components.SegmentComponentFragment
import dk.eboks.app.presentation.ui.senders.components.SenderComponentFragment
import dk.eboks.app.presentation.ui.senders.components.SenderListComponentFragment
import dk.eboks.app.presentation.ui.senders.screens.browse.SearchSendersActivity
import dk.eboks.app.presentation.ui.senders.screens.registrations.PendingActivity
import dk.eboks.app.presentation.ui.senders.screens.registrations.RegistrationsActivity
import kotlinx.android.synthetic.main.activity_senders_overview.*
import javax.inject.Inject
import kotlinx.android.synthetic.main.include_toolbar.*

class SendersOverviewActivity : BaseActivity(), SendersOverviewContract.View {

    @Inject
    lateinit var presenter: SendersOverviewContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_senders_overview)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
        setupTopBar()

        senderPendingBtn.setOnClickListener { v ->
            startActivity(Intent(this, PendingActivity::class.java))
        }
    }

    // TODO add translation
    private fun setupTopBar() {
        mainTb.navigationIcon = null
        mainTb.title = Translation.senders.title
        val menuRegist = mainTb.menu.add(Translation.senders.registrations)
        menuRegist.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
        menuRegist.setOnMenuItemClickListener { item: MenuItem ->
            startActivity(Intent(this, RegistrationsActivity::class.java ))
            true
        }
        val menuSearch = mainTb.menu.add("search")
        menuSearch.setIcon(R.drawable.search)
        menuSearch.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        menuSearch.setOnMenuItemClickListener { item: MenuItem ->
            startActivity(Intent(this, SearchSendersActivity::class.java ))
            true
        }
    }

    override fun showCollections(collections: List<CollectionContainer>) {
        sendersCollectionContainerLl.removeAllViews()

        collections.forEach {
            val b = Bundle()
            var f : BaseFragment? = null
            when (it.type) {
                "segment" -> {
                    b.putParcelable(CollectionContainer::class.simpleName, it)
                    f = SegmentComponentFragment()
                }
                "sender" -> {
                    b.putParcelable(Sender::class.simpleName, it.sender)
                    f = SenderComponentFragment()
                }
                "senders" -> {
                    b.putParcelable(CollectionContainer::class.simpleName, it)
                    f = SenderListComponentFragment()
                }
            }
            f?.let{
                it.arguments = b
                supportFragmentManager.beginTransaction().add(sendersCollectionContainerLl.id, it).commit()
            }
        }
    }
}
