package dk.eboks.app.presentation.ui.screens.senders.overview

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import dk.eboks.app.R
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.presentation.base.BaseActivity
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

    private fun setupTopBar() {
//
        mainTb.navigationIcon = null
//        mainTb.setNavigationIcon(R.drawable.search)
//        mainTb.setNavigationOnClickListener {
//            Toast.makeText(this, "Halloooooo", Toast.LENGTH_SHORT).show()
//        }
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
            Toast.makeText(this, "Search", Toast.LENGTH_SHORT).show()
            true
        }

    }

    override fun setupTranslations() {

    }

    override fun showError(msg: String) {
        Timber.e(msg) // errorhandling lol
    }
}
