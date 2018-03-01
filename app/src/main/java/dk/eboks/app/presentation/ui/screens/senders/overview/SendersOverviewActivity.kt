package dk.eboks.app.presentation.ui.screens.senders.overview

import android.os.Bundle
import dk.eboks.app.presentation.base.BaseActivity
import timber.log.Timber
import javax.inject.Inject

class SendersOverviewActivity : BaseActivity(), SendersOverviewContract.View {
    @Inject lateinit var presenter: SendersOverviewContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(dk.eboks.app.R.layout.activity_senders_overview)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
    }

    override fun setupTranslations() {

    }

    override fun showError(msg: String) {
        Timber.e(msg) // errorhandling lol
    }
}
