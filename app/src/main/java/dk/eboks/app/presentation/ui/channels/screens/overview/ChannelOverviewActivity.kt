package dk.eboks.app.presentation.ui.channels.screens.overview

import android.os.Bundle
import dk.eboks.app.R
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.presentation.base.BaseActivity
import dk.eboks.app.presentation.ui.channels.components.overview.ChannelOverviewComponentFragment
import javax.inject.Inject
import kotlinx.android.synthetic.main.include_toolbar.*

class ChannelOverviewActivity : BaseActivity(), ChannelOverviewContract.View {
    @Inject lateinit var presenter: ChannelOverviewContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_overview_channels)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
        setupTopBar()

        supportFragmentManager.addOnBackStackChangedListener {
            //Timber.e("bs changed entryCount ${supportFragmentManager.backStackEntryCount}")
            if (supportFragmentManager.backStackEntryCount == 0) {
                if (!isDestroyed)
                    finish()
            }
        }

        setRootFragment(R.id.containerFl, ChannelOverviewComponentFragment())
    }

    private fun setupTopBar() {
        mainTb.title = Translation.channels.title
    }

}
