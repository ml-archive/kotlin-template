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

        HintActivity.showHint(this, "This view containes the message read from the mock plus two seeded messages (alka, danske bank).\n- Tap item to open message in embedded mode (drawer)\n- Long press to open in non-embeddable mode\n- Pull to refresh")
    }

    override fun onResume() {
        super.onResume()
        NStack.translate(this@MailListActivity)
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
        toolbarTv.visibility = View.GONE
        toolbarLargeTv.visibility = View.VISIBLE
        toolbarLargeTv.text = "_All mail"
    }



    override fun showFolderName(name: String) {
        toolbarLargeTv.text = name
    }

    override fun showError(msg: String) {
        Log.e("debug", msg)
    }
}
