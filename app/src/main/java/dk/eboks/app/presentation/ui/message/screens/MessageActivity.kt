package dk.eboks.app.presentation.ui.message.screens

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import dk.eboks.app.R
import dk.eboks.app.domain.managers.EboksFormatter
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.folder.Folder
import dk.eboks.app.domain.models.folder.FolderType
import dk.eboks.app.domain.models.message.Message
import dk.eboks.app.domain.models.message.MessageType
import dk.eboks.app.presentation.base.BaseActivity
import dk.eboks.app.presentation.base.ViewerFragment
import dk.eboks.app.presentation.ui.folder.screens.FolderActivity
import dk.eboks.app.presentation.ui.message.components.detail.attachments.AttachmentsComponentFragment
import dk.eboks.app.presentation.ui.message.components.detail.document.DocumentComponentFragment
import dk.eboks.app.presentation.ui.message.components.detail.folderinfo.FolderInfoComponentFragment
import dk.eboks.app.presentation.ui.message.components.detail.header.HeaderComponentFragment
import dk.eboks.app.presentation.ui.message.components.detail.notes.NotesComponentFragment
import dk.eboks.app.presentation.ui.message.components.detail.reply.ReplyButtonComponentFragment
import dk.eboks.app.presentation.ui.message.components.detail.share.ShareComponentFragment
import dk.eboks.app.presentation.ui.overlay.screens.ButtonType
import dk.eboks.app.presentation.ui.overlay.screens.OverlayActivity
import dk.eboks.app.presentation.ui.overlay.screens.OverlayButton
import dk.eboks.app.presentation.ui.uploads.components.UploadOverviewComponentFragment
import dk.eboks.app.presentation.ui.uploads.screens.UploadsActivity
import kotlinx.android.synthetic.main.include_toolbar.*
import timber.log.Timber
import javax.inject.Inject

class MessageActivity : BaseActivity(), MessageContract.View {
    @Inject lateinit var presenter: MessageContract.Presenter
    @Inject
    lateinit var formatter: EboksFormatter

    var headerComponentFragment: HeaderComponentFragment? = null
    var replyButtonComponentFragment: ReplyButtonComponentFragment? = null
    var documentComponentFragment: DocumentComponentFragment? = null
    var shareComponentFragment: ShareComponentFragment? = null
    var notesComponentFragment: NotesComponentFragment? = null
    var attachmentsComponentFragment: AttachmentsComponentFragment? = null
    var folderInfoComponentFragment: FolderInfoComponentFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
        setContentView(R.layout.activity_message)
        setupTopBar()
        presenter.setup()
    }

    private var actionButtons = arrayListOf(
            OverlayButton(ButtonType.MOVE),
            OverlayButton(ButtonType.DELETE)
    )


    private fun setupTopBar() {
        mainTb.setNavigationIcon(R.drawable.icon_48_chevron_left_red_navigationbar)
        mainTb.title = Translation.message.title
        mainTb.setNavigationOnClickListener {
            onBackPressed()
        }
        val menuItem = mainTb?.menu?.add("_options")
        menuItem?.setIcon(R.drawable.icon_48_option_red_navigationbar)
        menuItem?.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        menuItem?.setOnMenuItemClickListener { item: MenuItem ->
            startActivityForResult(
                    OverlayActivity.createIntent(
                            this@MessageActivity,
                            actionButtons
                    ), OverlayActivity.REQUEST_ID
            )
            true
        }
    }

    override fun showTitle(message: Message) {
        mainTb.subtitle = formatter.formatDate(message)
    }

    override fun getNavigationMenuAction(): Int {
        return R.id.actionMail
    }

    override fun setActionButtons(message: Message) {

        if (message.type != MessageType.UPLOAD && message.folder?.type == FolderType.INBOX) {
            if (message.unread) actionButtons.add(OverlayButton(ButtonType.READ)) else actionButtons.add(OverlayButton(ButtonType.UNREAD))
            actionButtons.add(OverlayButton(ButtonType.ARCHIVE))
        }

    }

    override fun addHeaderComponentFragment() {
        headerComponentFragment = HeaderComponentFragment()
        headerComponentFragment?.let {
            it.arguments = Bundle().apply {
                putBoolean("show_divider", false)
            }
            supportFragmentManager.beginTransaction()
                .add(R.id.headerContainerLl, it, HeaderComponentFragment::class.java.simpleName)
                .commit()
        }
    }

    override fun addDocumentComponentFragment() {
        documentComponentFragment = DocumentComponentFragment()
        documentComponentFragment?.let {
            it.arguments = Bundle().apply {
                putBoolean("show_divider", true)
            }
            supportFragmentManager.beginTransaction()
                .add(R.id.headerContainerLl, it, DocumentComponentFragment::class.java.simpleName)
                .commit()
        }
    }

    override fun addReplyButtonComponentFragment(message: Message) {
        replyButtonComponentFragment = ReplyButtonComponentFragment()
        val args = Bundle()
        args.putParcelable(Message::class.java.simpleName, message)
        replyButtonComponentFragment?.let {
            it.arguments = args
            supportFragmentManager.beginTransaction()
                .add(R.id.bodyContainerLl, it, ReplyButtonComponentFragment::class.java.simpleName)
                .commit()
        }
    }

    override fun addShareComponentFragment() {
        /*shareComponentFragment = ShareComponentFragment()
        shareComponentFragment?.let {
            supportFragmentManager.beginTransaction()
                .add(R.id.bodyContainerLl, it, ShareComponentFragment::class.java.simpleName)
                .commit()
        }*/
    }

    override fun addNotesComponentFragment() {
        notesComponentFragment = NotesComponentFragment()
        notesComponentFragment?.let {
            supportFragmentManager.beginTransaction()
                .add(R.id.bodyContainerLl, it, NotesComponentFragment::class.java.simpleName)
                .commit()
        }
    }

    override fun addAttachmentsComponentFragment() {
        attachmentsComponentFragment = AttachmentsComponentFragment()
        attachmentsComponentFragment?.let {
            supportFragmentManager.beginTransaction()
                .add(R.id.bodyContainerLl, it, AttachmentsComponentFragment::class.java.simpleName)
                .commit()
        }
    }

    override fun addFolderInfoComponentFragment() {
        folderInfoComponentFragment = FolderInfoComponentFragment()
        folderInfoComponentFragment?.let {
            supportFragmentManager.beginTransaction()
                .add(R.id.footerContainerLl, it, FolderInfoComponentFragment::class.java.simpleName)
                .commit()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == FolderActivity.REQUEST_ID) {
            (data?.getSerializableExtra("res") as? Folder)?.let(presenter::moveMessage)
        }
        // Deal with return from document action sheet
        else if (requestCode == OverlayActivity.REQUEST_ID) {
            when (data?.getSerializableExtra("res")) {

                (ButtonType.MOVE) -> {
                    startFolderSelectActivity()
                }
                (ButtonType.ARCHIVE) -> {
                    presenter.archiveMessage()
                }
                (ButtonType.READ) -> {
                    presenter.markAsUnread(false)
                }
                (ButtonType.UNREAD) -> {
                    presenter.markAsUnread(true)
                }
                (ButtonType.DELETE) -> {
                    presenter.deleteMessage()
                }
                else -> {
                    // Request do nothing
                }
            }
        }
    }

    override fun messageDeleted() {
        UploadOverviewComponentFragment.refreshOnResume = true
        onBackPressed()
    }

    override fun updateFolderName(name: String) {
        folderInfoComponentFragment?.updateView(name)
    }

    private fun startFolderSelectActivity() {
        val i = Intent(this, FolderActivity::class.java)
        i.putExtra("pick", true)
        startActivityForResult(i, FolderActivity.REQUEST_ID)
    }
}
