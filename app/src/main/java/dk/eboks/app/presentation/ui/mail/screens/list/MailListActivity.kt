package dk.eboks.app.presentation.ui.mail.screens.list

import android.os.Bundle
import dk.eboks.app.R
import dk.eboks.app.domain.models.folder.Folder
import dk.eboks.app.domain.models.folder.FolderType
import dk.eboks.app.domain.models.sender.Sender
import dk.eboks.app.presentation.base.BaseActivity
import dk.eboks.app.presentation.ui.mail.components.maillist.MailListComponentFragment
import dk.eboks.app.util.putArg
import javax.inject.Inject

class MailListActivity : BaseActivity(), MailListContract.View,
    MailListComponentFragment.MailListComponentListener {
    @Inject lateinit var presenter: MailListContract.Presenter

    private val isUpploads: Boolean
        get() = (intent?.extras?.getSerializable("folder") as? Folder)?.type == FolderType.UPLOADS

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mail_list)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
        val frag = MailListComponentFragment()
        intent?.extras?.let { extras ->
            extras.getParcelable<Sender>("sender")?.let { sender ->
                presenter.setupSender(sender)
                frag.putArg("sender", sender)
            }

            (extras.getSerializable("folder") as? Folder)?.let { folder ->
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

    private fun setupTopBar(txt: String) {
        mainTb.setNavigationIcon(R.drawable.icon_48_chevron_left_red_navigationbar)
        mainTb.title = txt
        mainTb.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    override fun showFolderName(name: String) {
        setupTopBar(name)
    }

    override fun getNavigationMenuAction(): Int {
        return if (!isUpploads) R.id.actionMail else R.id.actionUploads
    }

    override fun onEditModeActive(active: Boolean) {
        navBarContainer.visible = !active
    }
}
