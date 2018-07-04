package dk.eboks.app.presentation.ui.mail.screens.list

import android.os.Bundle
import dk.eboks.app.R
import dk.eboks.app.domain.models.folder.Folder
import dk.eboks.app.domain.models.sender.Sender
import dk.eboks.app.presentation.base.BaseActivity
import dk.eboks.app.presentation.ui.mail.components.maillist.MailListComponentFragment
import dk.eboks.app.util.putArg
import javax.inject.Inject
import kotlinx.android.synthetic.main.include_toolbar.*

class MailListActivity : BaseActivity(), MailListContract.View {
    @Inject lateinit var presenter: MailListContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mail_list)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
        val frag = MailListComponentFragment()
        intent?.extras?.let { extras->
            if(extras.containsKey("sender"))
            {
                val sender = extras.getSerializable("sender") as Sender
                presenter.setupSender(sender)
                frag.putArg("sender", sender)
            }
            if(extras.containsKey("folder"))
            {
                val folder = extras.getSerializable("folder") as Folder
                presenter.setupFolder(folder)
                frag.putArg("folder", folder)
            }
        }

        setRootFragment(R.id.containerFl, frag)
        supportFragmentManager.addOnBackStackChangedListener {
            if (supportFragmentManager.backStackEntryCount == 0) {
                if (!isDestroyed)
                    finish()
            }
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
