package dk.eboks.app.presentation.ui.screens.mail.overview

import android.os.Bundle
import android.view.View
import dk.eboks.app.R
import dk.eboks.app.presentation.base.BaseActivity
import dk.eboks.app.presentation.ui.dialogs.ConfirmDialogFragment
import kotlinx.android.synthetic.main.activity_mail_overview.*
import kotlinx.android.synthetic.main.include_toolbar.*
import timber.log.Timber
import javax.inject.Inject


class MailOverviewActivity : BaseActivity(), MailOverviewContract.View {
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

        mainTb.setNavigationIcon(R.drawable.ic_menu_mail)
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
