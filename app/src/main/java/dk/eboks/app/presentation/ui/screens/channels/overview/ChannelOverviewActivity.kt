package dk.eboks.app.presentation.ui.screens.channels.overview

import android.os.Bundle
import dk.eboks.app.R
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.presentation.base.BaseActivity
import timber.log.Timber
import javax.inject.Inject

class ChannelOverviewActivity : BaseActivity(), ChannelOverviewContract.View {
    @Inject lateinit var presenter: ChannelOverviewContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_overview_channels)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
        setupToolbar()
    }

    private fun setupToolbar() {
        setToolbar(R.drawable.ic_menu_channels, Translation.channels.title,null, null ,true)
    }

    override fun setupTranslations() {

    }

    override fun showError(msg: String) {
        Timber.e(msg) // errorhandling lol
    }

}
