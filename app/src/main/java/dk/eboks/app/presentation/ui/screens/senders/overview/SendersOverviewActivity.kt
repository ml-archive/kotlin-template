package dk.eboks.app.presentation.ui.screens.senders.overview

import android.os.Bundle
import android.widget.ImageButton
import dk.eboks.app.R
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.presentation.base.BaseActivity
import kotlinx.android.synthetic.main.include_toolnar.*
import kotlinx.android.synthetic.main.include_toolnar.view.*
import timber.log.Timber
import javax.inject.Inject

class SendersOverviewActivity : BaseActivity(), SendersOverviewContract.View {
    @Inject lateinit var presenter: SendersOverviewContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(dk.eboks.app.R.layout.activity_senders_overview)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
        setupTopBar()
    }

    private fun setupTopBar() {
//
//        val v = ImageButton(this)
//        v.setImageResource(R.drawable.search)
//
//        mainTb.navigationIcon = null
//        mainTb.addView(v)
//        mainTb.title = Translation.senders.title

        setToolbar(R.drawable.search, Translation.senders.title, null, null,true, true, "REGISTRATIONS")
    }

    override fun setupTranslations() {

    }

    override fun showError(msg: String) {
        Timber.e(msg) // errorhandling lol
    }
}
