package dk.eboks.app.presentation.ui.screens.mail.list

import android.os.Bundle
import dk.eboks.app.R
import dk.eboks.app.presentation.base.BaseActivity
import kotlinx.android.synthetic.main.include_toolbar.*
import javax.inject.Inject

class MailListActivity : BaseActivity(), MailListContract.View {
    @Inject lateinit var presenter: MailListContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mail_list)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
    }

    override fun onShake() {
        if(showEmptyState)
        {
        }
        else
        {
        }
    }

    private fun setupTopBar(txt : String) {
        mainTb.setNavigationIcon(R.drawable.icon_48_chevron_left_red_navigationbar)
        mainTb.title = txt
        mainTb.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    override fun showFolderName(name: String) {
        setupTopBar(name)
    }

    override fun getNavigationMenuAction(): Int { return R.id.actionMail }
}
