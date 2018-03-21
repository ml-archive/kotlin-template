package dk.eboks.app.presentation.ui.screens.senders.overview

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import dk.eboks.app.R
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.sender.CollectionContainer
import dk.eboks.app.domain.models.sender.Segment
import dk.eboks.app.domain.models.sender.Sender
import dk.eboks.app.presentation.base.BaseActivity
import dk.eboks.app.presentation.base.BaseFragment
import dk.eboks.app.presentation.ui.components.senders.SegmentComponentFragment
import dk.eboks.app.presentation.ui.components.senders.SenderComponentFragment
import dk.eboks.app.presentation.ui.components.senders.SenderListComponentFragment
import dk.eboks.app.presentation.ui.screens.senders.browse.SearchSendersActivity
import kotlinx.android.synthetic.main.activity_senders_overview.*
import kotlinx.android.synthetic.main.include_toolbar.*
import timber.log.Timber
import javax.inject.Inject

class SendersOverviewActivity : BaseActivity(), SendersOverviewContract.View {

    @Inject
    lateinit var presenter: SendersOverviewContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(dk.eboks.app.R.layout.activity_senders_overview)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
        setupTopBar()
    }

    // TODO add translation
    private fun setupTopBar() {
        mainTb.navigationIcon = null
        mainTb.title = Translation.senders.title
        val menuRegist = mainTb.menu.add("Registrations")
        menuRegist.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
        menuRegist.setOnMenuItemClickListener { item: MenuItem ->
            Toast.makeText(this, "Registrations", Toast.LENGTH_SHORT).show()
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
            lateinit var f : BaseFragment
            when (it.type) {
                "segment" -> {
                    b.putSerializable(Segment::class.simpleName, it.segment)
                    f = SegmentComponentFragment()
                    f.arguments = b
                    supportFragmentManager.beginTransaction().add(sendersCollectionContainerLl.id, f).commit()
                }
                "sender" -> {
                    b.putSerializable(Sender::class.simpleName, it.sender)
                    f = SenderComponentFragment()
                    f.arguments = b
                    supportFragmentManager.beginTransaction().add(sendersCollectionContainerLl.id, f).commit()
                }
                "senders" -> {
                    b.putSerializable(CollectionContainer::class.simpleName, it)
                    f = SenderListComponentFragment()
                    f.arguments = b
                    supportFragmentManager.beginTransaction().add(sendersCollectionContainerLl.id, f).commit()
                }
            }

        }
    }

    override fun setupTranslations() {

    }

    override fun showError(msg: String) {
        Timber.e(msg) // errorhandling lol
    }
}
