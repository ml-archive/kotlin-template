package dk.eboks.app.presentation.ui.screens.mail.list

import android.os.Bundle
import android.util.Log
import android.view.View
import dk.eboks.app.R
import dk.eboks.app.presentation.base.BaseActivity
import dk.eboks.app.presentation.ui.screens.debug.hinter.HintActivity
import dk.nodes.nstack.kotlin.NStack
import kotlinx.android.synthetic.main.include_toolnar.*
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

    override fun setupTranslations() {
    }

    override fun showFolderName(name: String) {
        setToolbar(R.drawable.red_navigationbar, name,null, {onBackPressed()})
    }

    override fun showError(msg: String) {
        Log.e("debug", msg)
    }
}
