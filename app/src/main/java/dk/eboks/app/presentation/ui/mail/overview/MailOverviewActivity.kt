package dk.eboks.app.presentation.ui.mail.overview

import android.os.Bundle
import android.view.View
import dk.eboks.app.R
import dk.eboks.app.presentation.base.MainNavigationBaseActivity
import dk.eboks.app.presentation.ui.debug.hinter.HintActivity
import dk.eboks.app.presentation.ui.dialogs.ConfirmDialogFragment
import kotlinx.android.synthetic.main.activity_mail_overview.*
import kotlinx.android.synthetic.main.include_toolnar.*
import timber.log.Timber
import javax.inject.Inject


class MailOverviewActivity : MainNavigationBaseActivity(), MailOverviewContract.View {
    @Inject lateinit var presenter: MailOverviewContract.Presenter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mail_overview)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
        //setupYourMail()


        refreshSrl.setOnRefreshListener {
            presenter.refresh()
        }

        userShareTv.setOnClickListener {
            showConfirmDialog()
        }

        userShareTv.visibility = View.VISIBLE

        HintActivity.showHint(this, "- Shake to show empty state\n- Tap 'you mail' to preview confirm dialog\n- Folders open the treeview the rest opens the individual list views\n- Pull to refresh")
    }

    override fun setupTranslations() {

    }

    fun showConfirmDialog()
    {
        Timber.e("Showing confirm dialog")
        var dialog = ConfirmDialogFragment()
        dialog.show(supportFragmentManager, ConfirmDialogFragment::class.simpleName)
    }

    override fun showProgress(show: Boolean) {
        if(refreshSrl.isRefreshing != show)
            refreshSrl.isRefreshing = show
    }


}
